package org.zeith.hammerlib.util;

import net.minecraft.Util;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.mcf.ModHelper;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
		return LINKS.get(link.key());
	}
	
	public static Optional<String> findLink(String key)
	{
		return Optional.ofNullable(LINKS.get(key));
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
	
	private static final Map<String, String> LINKS = Util.make(new HashMap<>(), m ->
	{
		final List<String> SOURCES = List.of(
				"https://pastebin.com/raw/Wtfb9h01",
				"https://mods.zeith.org/static/link_repository.json"
		);
		
		CompletableFuture.runAsync(() ->
		{
			for(var src : SOURCES)
			{
				try(var req = HttpRequest.get(src)
						.acceptJson()
						.userAgent("HammerLib " + ModHelper.getModVersion(HLConstants.MOD_ID) + "; Minecraft")
						.connectTimeout(60000)
						.readTimeout(60000)
				)
				{
					if(req.ok())
					{
						for(var e : JSONVisitor.expand(new JSONTokener(req.body()).nextValue(), "/", false).entrySet())
							m.put(e.getKey(), Objects.toString(e.getValue()));
						return;
					}
				} catch(Exception e)
				{
					// NO-OP
				}
			}
			
			try
			{
				for(var e : JSONVisitor.expand(new JSONTokener(DEFAULT_JSON).nextValue(), "/", false).entrySet())
					m.put(e.getKey(), Objects.toString(e.getValue()));
			} catch(Exception e)
			{
				// NO-OP
			}
		});
	});
}