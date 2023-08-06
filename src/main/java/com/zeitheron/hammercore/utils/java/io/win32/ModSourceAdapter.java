package com.zeitheron.hammercore.utils.java.io.win32;

import com.zeitheron.hammercore.lib.zlib.json.*;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;
import com.zeitheron.hammercore.utils.java.StreamHelper;
import net.minecraftforge.fml.common.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.*;

public class ModSourceAdapter
{
	/**
	 * This lazy list contains a list of up-to-date illegal websites which allows to check with {@link ModSource}
	 */
	public static final List<IllegalSite> ILLEGAL_SITES;
	
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
			
			return ZoneIdentifier.forFile(modFile).map(ModSource::new);
		} catch(Throwable err)
		{
			err.printStackTrace();
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
		
		public boolean wasDownloadedIllegally()
		{
			try
			{
				List<URL> urls = Arrays.asList(new URL(referrerUrl), new URL(hostUrl));
				return ILLEGAL_SITES.stream().anyMatch(site -> urls.stream().anyMatch(site));
			} catch(Throwable ignored)
			{
			}
			
			return false;
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
	
	static
	{
		List<IllegalSite> illegalSites = Collections.emptyList();
		
		try
		{
			illegalSites = StreamHelper.optionalStream(new JSONTokener(
							HttpRequest.get("https://api.stopmodreposts.org/minecraft/sites.json")
									.userAgent("HammerLib")
									.body())
							.nextValueARR())
					.flatMap(array ->
							IntStream.range(0, array.size())
									.mapToObj(array::getJSONObject)
									.map(IllegalSite::new))
					.collect(Collectors.toList());
		} catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		ILLEGAL_SITES = illegalSites;
	}
}