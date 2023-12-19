package org.zeith.hammerlib.util;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.mcf.ModHelper;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

public class ZeithLinkRepository
{
	public static void initialize()
	{
	}
	
	public enum PredefinedLink
	{
		DEV_DISCORD_CARD_IMAGE("discord/dev/card"),
		DEV_DISCORD_INVITE("discord/dev/invite");
		
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
	
	private static final String DEFAULT_JSON = """
			{
			  "discord": {
			    "dev": {
			      "card": "https://ds.zeith.org/cards/dev.png",
			      "invite": "https://ds.zeith.org/dev"
			    }
			  },
			  "mods": {
			    "improvableskills": {
			      "news": "https://mods.zeith.org/improvableskills/news.txt"
			    }
			  }
			}
			""";
	
	private interface Storage
	{
		Map<String, String> LINKS = new HashMap<>()
		{
			{
				final List<String> SOURCES = List.of(
						"https://pastebin.com/raw/Wtfb9h01",
						"https://gist.githubusercontent.com/Zeitheron/78b4f4745cbf14b3d5974ee7ef437249/raw/link_repository.json",
						"https://mods.zeith.org/static/link_repository.json"
				);
				
				Consumer<String> loader = json ->
				{
					for(var e : JSONVisitor.expand(new JSONTokener(json).nextValue(), "/", false).entrySet())
						super.put(e.getKey(), Objects.toString(e.getValue()));
				};
				
				try
				{
					loader.accept(DEFAULT_JSON); // insta-load the default links baked into the mod, and then fetch more links.
				} catch(Exception e)
				{
					// NO-OP
				}
				
				CompletableFuture.runAsync(() ->
				{
					
					for(var src : SOURCES)
					{
						try(var req = HttpRequest.get(src)
								.acceptJson()
								.userAgent("HammerLib " + ModHelper.getModVersion(HLConstants.MOD_ID) + "; Minecraft " + FMLLoader.versionInfo().mcVersion())
								.connectTimeout(60000)
								.readTimeout(60000)
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
				return Set.copyOf(super.entrySet());
			}
			
			@Override
			public Set<String> keySet()
			{
				return Set.copyOf(super.keySet());
			}
			
			@Override
			public Collection<String> values()
			{
				return List.copyOf(super.values());
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