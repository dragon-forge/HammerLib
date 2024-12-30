package org.zeith.hammerlib.util.mcf;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.loading.FMLPaths;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.data.JSONHelper;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class TagRegistrationContext
{
	private final Path file;
	private boolean changed;
	
	private final Map<ResourceLocation, Map<ResourceLocation, TagData>> storage = new HashMap<>();
	
	public TagRegistrationContext(Path file)
	{
		this.file = file;
	}
	
	private void markChanged()
	{
		changed = true;
	}
	
	protected final Runnable markChanged = this::markChanged;
	
	protected TagData create(TagKey<?> tag)
	{
		return new TagData(tag, new Object2BooleanOpenHashMap<>(), markChanged);
	}
	
	protected Map<ResourceLocation, TagData> getTagMapForRegistry(ResourceLocation registry)
	{
		return storage.computeIfAbsent(registry, t -> new HashMap<>());
	}
	
	protected TagData getTagData(TagKey<?> tag)
	{
		return getTagMapForRegistry(tag.registry().location())
				.computeIfAbsent(tag.location(), t -> create(tag));
	}
	
	public boolean addToTag(TagKey<?> tag, ResourceLocation item)
	{
		return getTagData(tag).enableItemInTag(item);
	}
	
	public static TagRegistrationContext load(String modid)
	{
		var recipes = FMLPaths.CONFIGDIR.get().resolve(HLConstants.MOD_ID)
				.resolve("tags")
				.resolve("modded")
				.resolve(modid + ".json");
		
		final var ctx = new TagRegistrationContext(recipes);
		
		try
		{
			Files.createDirectories(recipes.getParent());
			if(Files.isRegularFile(recipes))
			{
				new JSONTokener(Files.readString(recipes)).nextValueOBJ().ifPresent(obj ->
				{
//					var vers = obj.optInt("version");
					var $ = obj.getJSONObject("tags");
					for(String registry : $.keySet())
					{
						var regId = Resources.location(registry);
						var regKey = ResourceKey.createRegistryKey(regId);
						
						var $$ = $.optJSONObject(registry);
						if($$ != null)
							for(String key : $$.keySet())
							{
								var tagId = Resources.location(key);
								TagData td = ctx.getTagData(TagKey.create(regKey, tagId));
								var $$$ = $$.optJSONObject(key);
								if($$$ != null)
									for(String itemId : $$$.keySet())
										td.active.put(itemId, $$$.optBoolean(itemId, true));
							}
					}
				});
			}
		} catch(IOException err)
		{
			log.error("Failed to load tag configs", err);
		}
		
		return ctx;
	}
	
	public void save()
	{
		if(changed)
		{
			var activeGlob = new JSONObject();
			var disabledGlob = new JSONObject();
			
			Function<String, JSONObject> activeByReg = type -> JSONHelper.computeObject(activeGlob, type);
			
			for(var entry : storage.entrySet())
			{
				var registryKey = entry.getKey().toString();
				var active = Suppliers.memoize(() -> activeByReg.apply(registryKey));
				
				for(var tag : entry.getValue().entrySet())
				{
					var tagId = tag.getKey().toString();
					var data = tag.getValue();
					
					var activeTag = Suppliers.memoize(() -> JSONHelper.computeObject(active.get(), tagId));
					
					for(var st : data.active.object2BooleanEntrySet())
						activeTag
								.get()
								.put(st.getKey(), st.getBooleanValue());
				}
			}
			
			try
			{
				Files.createDirectories(file.getParent());
				Files.writeString(file,
						new JSONObject()
								.put("version", 1)
								.put("tags", activeGlob)
								.put("unregistered", disabledGlob)
								.toString(4)
				);
			} catch(IOException e)
			{
				log.error("Failed to write tag configs", e);
			}
		}
	}
	
	protected record TagData(TagKey<?> type, Object2BooleanMap<String> active, Runnable changed)
	{
		public boolean enableItemInTag(ResourceLocation loc)
		{
			var id = loc.toString();
			
			if(!active.containsKey(id))
			{
				active.put(id, true);
				changed.run();
			}
			
			return active.getBoolean(id);
		}
	}
}