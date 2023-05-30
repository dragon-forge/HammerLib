package org.zeith.hammerlib.util.mcf;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.core.adapter.recipe.RecipeBuilder;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RecipeRegistrationContext
{
	private final Path file;
	private final Object2BooleanMap<String> allRecipes;
	private final Set<String> usedKeys = new HashSet<>();
	private boolean changed;
	
	public RecipeRegistrationContext(Path file, Object2BooleanMap<String> allRecipes)
	{
		this.file = file;
		this.allRecipes = allRecipes;
	}
	
	public boolean enableRecipe(ResourceLocation id)
	{
		return enableRecipe(id.toString());
	}
	
	public boolean enableRecipe(String id)
	{
		usedKeys.add(id); // mark the key as used.
		if(!allRecipes.containsKey(id))
		{
			allRecipes.put(id, true);
			changed = true;
		}
		return allRecipes.getBoolean(id);
	}
	
	public <RES, T extends RecipeBuilder<T, RES>> Optional<T> register(@NotNull T builder)
	{
		if(enableRecipe(builder.getIdentifier()))
		{
			builder.register();
			return Optional.of(builder);
		}
		return Optional.empty();
	}
	
	public static RecipeRegistrationContext load(String modid)
	{
		var recipes = FMLPaths.CONFIGDIR.get().resolve(HLConstants.MOD_ID).resolve("recipes").resolve("modded").resolve(modid + ".json");
		
		final Object2BooleanMap<String> enabledRecipes = new Object2BooleanArrayMap<>();
		try
		{
			Files.createDirectories(recipes.getParent());
			if(Files.isRegularFile(recipes))
			{
				new JSONTokener(Files.readString(recipes)).nextValueOBJ().ifPresent(obj ->
				{
					var $ = obj.getJSONObject("active");
					for(String key : $.keySet())
						enabledRecipes.put(key, $.optBoolean(key, true));
					
					$ = obj.getJSONObject("unregistered");
					for(String key : $.keySet())
						enabledRecipes.put(key, $.optBoolean(key, true));
				});
			}
		} catch(IOException err)
		{
			err.printStackTrace();
		}
		
		return new RecipeRegistrationContext(recipes, enabledRecipes);
	}
	
	public void save()
	{
		if(changed)
		{
			var active = new JSONObject();
			var disabled = new JSONObject();
			
			for(var entry : allRecipes.object2BooleanEntrySet())
				(usedKeys.contains(entry.getKey()) ? active : disabled)
						.put(entry.getKey(), entry.getBooleanValue());
			
			try
			{
				Files.createDirectories(file.getParent());
				Files.writeString(file,
						new JSONObject()
								.put("active", active)
								.put("unregistered", disabled)
								.toString(4)
				);
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}