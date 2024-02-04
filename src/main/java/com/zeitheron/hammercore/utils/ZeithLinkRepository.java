package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.HLConstants;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;
import com.zeitheron.hammercore.utils.forge.ModHelper;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

public class ZeithLinkRepository
{
	private static CompletableFuture<?> load = CompletableFuture.completedFuture(null);
	
	public static void initialize()
	{
		Storage.LINKS.clear();
	}
	
	public static void finishLoading()
	{
		load.join();
	}
	
	public enum PredefinedLink
	{
		DEV_DISCORD_CARD_IMAGE("discord/dev/card"),
		DEV_DISCORD_INVITE("discord/dev/invite"),
		DAY_SPLASHES("mods/hammerlib/day_splashes");
		
		private final String key;
		
		PredefinedLink(String key)
		{
			this.key = key;
		}
		
		public String key()
		{
			return key;
		}
	}
	
	public static String getLink(PredefinedLink link)
	{
		return Storage.LINKS.get(link.key());
	}
	
	public static Optional<String> findLink(String key)
	{
		return Optional.ofNullable(Storage.LINKS.get(key));
	}
	
	public static Optional<String> findModLink(ResourceLocation path)
	{
		return findLink("mods/" + path.getNamespace() + "/" + path.getPath());
	}
	
	private static final String DEFAULT_JSON =
			"{\n" +
			"  \"discord\": {\n" +
			"    \"dev\": {\n" +
			"      \"card\": \"https://ds.zeith.org/cards/dev.png\",\n" +
			"      \"invite\": \"https://ds.zeith.org/dev\"\n" +
			"    }\n" +
			"  },\n" +
			"  \"mods\": {\n" +
			"    \"improvableskills\": {\n" +
			"      \"news\": \"https://assets.zeith.org/txt/is3-news.txt\"\n" +
			"    },\n" +
			"    \"hammerlib\": {\n" +
			"        \"day_splashes\": \"https://mods.zeith.org/static/day_splashes.json\"\n" +
			"    }\n" +
			"  }\n" +
			"}";
	
	private interface Storage
	{
		Map<String, String> LINKS = new HashMap<String, String>()
		{
			{
				final List<String> SOURCES = Arrays.asList(
						"https://pastebin.com/raw/Wtfb9h01",
						"https://gist.githubusercontent.com/Zeitheron/78b4f4745cbf14b3d5974ee7ef437249/raw/link_repository.json",
						"https://mods.zeith.org/static/link_repository.json"
				);
				
				Consumer<String> loader = json ->
				{
					for(Map.Entry<String, Object> e : JSONVisitor.expand(new JSONTokener(json).nextValue(), "/", false).entrySet())
						super.put(e.getKey(), Objects.toString(e.getValue()));
				};
				
				try
				{
					loader.accept(DEFAULT_JSON); // insta-load the default links baked into the mod, and then fetch more links.
				} catch(Exception e)
				{
					// NO-OP
				}
				
				load = CompletableFuture.runAsync(() ->
				{
					for(String src : SOURCES)
					{
						try(HttpRequest req = HttpRequest.get(src)
								.acceptJson()
								.userAgent("HammerLib " + ModHelper.getModVersion(HLConstants.MODID) + "; Minecraft 1.12.2")
								.connectTimeout(5000)
								.readTimeout(5000)
						)
						{
							if(req.ok())
							{
								loader.accept(req.body());
								return;
							}
						} catch(Exception e)
						{
							// NO-OP
						}
					}
				});
			}
			
			@Override
			public String put(String key, String value)
			{
				return get(key);
			}
			
			@Override
			public void putAll(Map<? extends String, ? extends String> m)
			{
			}
			
			@Override
			public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction)
			{
				return get(key);
			}
			
			@Override
			public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction)
			{
				return get(key);
			}
			
			@Override
			public String merge(String key, String value, BiFunction<? super String, ? super String, ? extends String> remappingFunction)
			{
				return get(key);
			}
			
			@Override
			public void clear()
			{
			}
			
			@Override
			public boolean remove(Object key, Object value)
			{
				return false;
			}
			
			@Override
			public String remove(Object key)
			{
				return get(key);
			}
			
			@Override
			public Set<Entry<String, String>> entrySet()
			{
				return Collections.unmodifiableSet(super.entrySet());
			}
			
			@Override
			public Set<String> keySet()
			{
				return Collections.unmodifiableSet(super.keySet());
			}
			
			@Override
			public Collection<String> values()
			{
				return Collections.unmodifiableCollection(super.values());
			}
			
			@Override
			public boolean replace(String key, String oldValue, String newValue)
			{
				return false;
			}
			
			@Override
			public String replace(String key, String value)
			{
				return value;
			}
			
			@Override
			public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function)
			{
			}
		};
	}
}