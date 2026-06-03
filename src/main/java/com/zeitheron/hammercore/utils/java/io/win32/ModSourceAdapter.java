package com.zeitheron.hammercore.utils.java.io.win32;

import com.zeitheron.hammercore.HLConstants;
import com.zeitheron.hammercore.lib.zlib.json.*;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;
import com.zeitheron.hammercore.utils.java.*;
import net.minecraftforge.fml.common.*;
import org.apache.logging.log4j.*;
import org.zeith.hammerlib.util.mcf.McUtil;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.*;

public class ModSourceAdapter
{
	private static final Logger LOG = LogManager.getLogger("HammerLib/ModSourceAdapter");
	
	/**
	 * This lazy list contains a list of up-to-date illegal websites which allows to check with {@link ModSource}
	 */
	public static final CompletableFuture<List<IllegalSite>> ILLEGAL_SITES = load();
	
	/**
	 * Gets the source from where the given mod was downloaded.
	 * Note: For now, it only works on Windows/NTFS.
	 * <p>
	 * PLEASE DO NOT DO ANYTHING IF THE OPTIONAL IS EMPTY!
	 */
	public static Optional<ModSource> getModSource(String modId)
	{
		Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
		return StreamHelper.optionalStream(Optional.ofNullable(mods.get(modId)))
		                   .map(ModContainer::getSource)
		                   .map(ZoneIdentifier::forFileSafe)
		                   .flatMap(StreamHelper::optionalStream)
		                   .map(ModSource::new)
		                   .findFirst();
	}
	
	/**
	 * Gets the source from where the given mod was downloaded.
	 * Note: For now, it only works on Windows/NTFS.
	 * <p>
	 * PLEASE DO NOT DO ANYTHING IF THE OPTIONAL IS EMPTY!
	 */
	public static Optional<ModSource> getModSource(Class<?> modClass)
	{
		try
		{
			File modFile = null;
			Mod mod = modClass.getDeclaredAnnotation(Mod.class);
			
			Map<String, ModContainer> mods = Loader.instance().getIndexedModList();
			
			if(mod != null)
			{
				ModContainer mc = mods.get(mod.modid());
				modFile = mc.getSource();
			}
			
			if(modFile == null)
				modFile = new File(modClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			
			return ZoneIdentifier.forFileSafe(modFile).map(ModSource::new);
		} catch(Throwable err)
		{
			LOG.error(err);
		}
		
		return Optional.empty();
	}
	
	public static class ModSource
	{
		public final String referrerUrl, hostUrl;
		
		public ModSource(String referrerUrl, String hostUrl)
		{
			this.referrerUrl = referrerUrl;
			this.hostUrl = hostUrl;
		}
		
		public ModSource(ZoneIdentifier id)
		{
			this(id.referrerUrl, id.hostUrl);
		}
		
		public CompletableFuture<Boolean> wasDownloadedIllegally()
		{
			try
			{
				List<URL> urls = Arrays.asList(new URL(referrerUrl), new URL(hostUrl));
				return ILLEGAL_SITES.thenApply(f -> f.stream().anyMatch(site -> urls.stream().anyMatch(site)));
			} catch(Throwable ignored)
			{
			}
			return CompletableFuture.completedFuture(false);
		}
		
		public String referrerDomain()
		{
			try
			{
				return new URL(referrerUrl).getHost();
			} catch(MalformedURLException ignored)
			{
			}
			
			return referrerUrl;
		}
	}
	
	public static class IllegalSite
			implements Predicate<URL>
	{
		public final String domain, notes, path, reason;
		
		public IllegalSite(String domain, String notes, String path, String reason)
		{
			this.domain = domain;
			this.notes = notes;
			this.path = path;
			this.reason = reason;
		}
		
		public IllegalSite(JSONObject object)
		{
			this(object.getString("domain"), object.optString("notes"), object.optString("path"), object.optString("reason"));
		}
		
		@Override
		public boolean test(URL url)
		{
			return (url.getHost().equalsIgnoreCase(domain) || url.getHost().endsWith("." + domain))
					&& url.getPath().startsWith(path);
		}
	}
	
	public static void bootstrap()
	{
	}
	
	private static CompletableFuture<List<IllegalSite>> load()
	{
		List<CompletableFuture<List<IllegalSite>>> options = new ArrayList<>();
		
		// First source of truth
		options.add(fetchSites("https://api.stopmodreposts.org/minecraft/sites.json", 30_000));
		
		// Might be out of date
		options.add(fetchSites("https://assets.zeith.org/stopmodreposts/sites.json", 30_000));
		
		// Test
		options.add(CompletableFuture.supplyAsync(() ->
				{
					try(InputStream in = ModSourceAdapter.class.getResourceAsStream("/META-INF/stopmodreposts/sites.json"))
					{
						return parse((JSONArray) new JSONTokener(in).nextValue());
					} catch(Exception e)
					{
						LOG.error("Missing/corrupted internal json file.", e);
						throw new CompletionException(new FileNotFoundException("Missing/corrupted internal json file."));
					}
				}, McUtil.backgroundExecutor()
		));
		
		return FuturesUtil.firstSuccessfulInOrder(options, idx -> LOG.info("Loaded from source {}", idx));
	}
	
	private static CompletableFuture<List<IllegalSite>> fetchSites(String url, int timeoutMs)
	{
		return CompletableFuture.supplyAsync(() -> parse(new JSONArray(HttpRequest
						.get(url)
						.userAgent("Minecraft/1.12.2 HammerLib/" + HLConstants.VERSION)
						.connectTimeout(timeoutMs)
						.readTimeout(timeoutMs)
						.body()
				)),
				McUtil.backgroundExecutor()
		);
	}
	
	private static List<IllegalSite> parse(JSONArray array)
	{
		return IntStream
				.range(0, array.size())
				.mapToObj(array::getJSONObject)
				.map(IllegalSite::new)
				.collect(Collectors.toList());
	}
}