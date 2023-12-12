package org.zeith.hammerlib.util.mcf;

import it.unimi.dsi.fastutil.objects.*;
import lombok.var;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.shaded.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class RecipeRegistrationContext
{
	public static final Logger LOG = LogManager.getLogger(RecipeRegistrationContext.class);
	private final Path file;
	private boolean changed;
	
	private final Map<String, RecipesData> allRecipes = new HashMap<>();
	
	public RecipeRegistrationContext(Path file)
	{
		this.file = file;
	}
	
	private void markChanged()
	{
		changed = true;
	}
	
	protected final Runnable markChanged = this::markChanged;
	
	protected RecipesData create(String key)
	{
		return new RecipesData(key, new Object2BooleanArrayMap<>(), new HashSet<>(), markChanged);
	}
	
	public boolean enableRecipe(IRecipeType<?> type, ResourceLocation id)
	{
		var idKey = id.toString();
		var rt = Registry.RECIPE_TYPE.getKey(type);
		var key = rt == null ? "<unknown>" : rt.toString();
		
		var unknown = allRecipes.get("<unknown>");
		var data = allRecipes.computeIfAbsent(key, this::create);
		
		if(unknown != null && unknown.active.containsKey(idKey))
			data.active.put(idKey, unknown.active.removeBoolean(idKey));
		
		return data.enableRecipe(idKey);
	}
	
	public <T extends RecipeBuilder<T>> Optional<T> register(IRecipeType<?> type, @NotNull T builder)
	{
		if(enableRecipe(type, builder.getIdentifier()))
		{
			builder.register();
			return Optional.of(builder);
		}
		return Optional.empty();
	}
	
	public static RecipeRegistrationContext load(String modid)
	{
		var recipes = FMLPaths.CONFIGDIR.get().resolve(HLConstants.MOD_ID)
				.resolve("recipes")
				.resolve("modded")
				.resolve(modid + ".json");
		
		final var ctx = new RecipeRegistrationContext(recipes);
		
		try
		{
			Files.createDirectories(recipes.getParent());
			if(Files.isRegularFile(recipes))
			{
				new JSONTokener(new String(Files.readAllBytes(recipes), StandardCharsets.UTF_8)).nextValueOBJ().ifPresent(obj ->
				{
					var vers = obj.optInt("version");
					if(vers == 0) // old adapter
					{
						final Object2BooleanMap<String> toResolve = new Object2BooleanArrayMap<>();
						JSONObject[] objs = {
								obj.getJSONObject("active"),
								obj.getJSONObject("unregistered")
						};
						for(var $ : objs)
							for(String key : $.keySet())
								toResolve.put(key, $.optBoolean(key, true));
						ctx.allRecipes
								.computeIfAbsent("<unknown>", ctx::create)
								.active
								.putAll(toResolve);
						
						ctx.markChanged(); // upgrade!
						
						LOG.info("Upgrade recipe registration context of {} to v2", recipes.getFileName());
						return;
					}
					
					if(vers == 2) // current adapter
					{
						JSONObject[] objs = {
								obj.getJSONObject("active"),
								obj.getJSONObject("unregistered")
						};
						for(var $ : objs)
							for(String type : $.keySet())
							{
								var storage = ctx.allRecipes.computeIfAbsent(type, ctx::create);
								var $$ = $.optJSONObject(type);
								if($$ != null)
									for(String key : $$.keySet())
										storage.active.put(key, $$.optBoolean(key, true));
							}
					}
				});
			}
		} catch(IOException err)
		{
			err.printStackTrace();
		}
		
		return ctx;
	}
	
	public void save()
	{
		if(changed)
		{
			var activeGlob = new JSONObject();
			var disabledGlob = new JSONObject();
			
			Function<String, JSONObject> activeByType = type ->
			{
				if(activeGlob.has(type))
					return activeGlob.getJSONObject(type);
				return activeGlob.put(type, new JSONObject())
						.getJSONObject(type);
			};
			Function<String, JSONObject> disabledByType = type ->
			{
				if(disabledGlob.has(type))
					return disabledGlob.getJSONObject(type);
				return disabledGlob.put(type, new JSONObject())
						.getJSONObject(type);
			};
			
			for(var entry : allRecipes.entrySet())
			{
				var e = entry.getValue();
				if(e.active.isEmpty()) continue; // skip empty bodies.
				
				var active = LazyOptional.of(() -> activeByType.apply(entry.getKey()));
				var disabled = LazyOptional.of(() -> disabledByType.apply(entry.getKey()));
				
				for(var st : e.active.object2BooleanEntrySet())
					(e.usedKeys.contains(st.getKey()) ? active : disabled)
							.resolve().orElse(new JSONObject())
							.put(st.getKey(), st.getBooleanValue());
			}
			
			try
			{
				Files.createDirectories(file.getParent());
				Files.write(file, new JSONObject()
						.put("version", 2)
						.put("active", activeGlob)
						.put("unregistered", disabledGlob)
						.toString(4)
						.getBytes(StandardCharsets.UTF_8));
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected final class RecipesData
	{
		private final String type;
		private final Object2BooleanMap<String> active;
		private final Set<String> usedKeys;
		private final Runnable changed;
		
		protected RecipesData(String type, Object2BooleanMap<String> active, Set<String> usedKeys, Runnable changed)
		{
			this.type = type;
			this.active = active;
			this.usedKeys = usedKeys;
			this.changed = changed;
		}
		
		public boolean enableRecipe(String id)
		{
			usedKeys.add(id); // mark the key as used.
			if(!active.containsKey(id))
			{
				active.put(id, true);
				changed.run();
			}
			return active.getBoolean(id);
		}
		
		public String type() {return type;}
		
		public Object2BooleanMap<String> active() {return active;}
		
		public Set<String> usedKeys() {return usedKeys;}
		
		public Runnable changed() {return changed;}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this) return true;
			if(obj == null || obj.getClass() != this.getClass()) return false;
			RecipesData that = (RecipesData) obj;
			return Objects.equals(this.type, that.type) &&
				   Objects.equals(this.active, that.active) &&
				   Objects.equals(this.usedKeys, that.usedKeys) &&
				   Objects.equals(this.changed, that.changed);
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(type, active, usedKeys, changed);
		}
		
		@Override
		public String toString()
		{
			return "RecipesData[" +
				   "type=" + type + ", " +
				   "active=" + active + ", " +
				   "usedKeys=" + usedKeys + ", " +
				   "changed=" + changed + ']';
		}
	}
}