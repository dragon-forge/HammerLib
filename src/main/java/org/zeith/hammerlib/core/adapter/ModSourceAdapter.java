package org.zeith.hammerlib.core.adapter;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import org.zeith.hammerlib.util.java.io.win32.ZoneIdentifier;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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
		return ModList.get()
				.getModContainerById(modId)
				.map(ModContainer::getModInfo)
				.map(IModInfo::getOwningFile)
				.map(IModFileInfo::getFile)
				.map(IModFile::getFilePath)
				.map(Path::toFile)
				.flatMap(ZoneIdentifier::forFileSafe)
				.map(ModSource::new);
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
			if(mod != null) modFile = ModList.get().getModFileById(mod.value()).getFile().getFilePath().toFile();
			if(modFile == null) modFile = Path.of(modClass.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
			return ZoneIdentifier.forFile(modFile).map(ModSource::new);
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	public record ModSource(String referrerUrl, String hostUrl)
	{
		public ModSource(ZoneIdentifier id)
		{
			this(id.referrerUrl, id.hostUrl);
		}
		
		public boolean wasDownloadedIllegally()
		{
			try
			{
				var urls = List.of(new URL(referrerUrl), new URL(hostUrl));
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
	
	public record IllegalSite(String domain, String notes, String path, String reason)
			implements Predicate<URL>
	{
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
		List<IllegalSite> illegalSites = List.of();
		
		try
		{
			illegalSites = new JSONTokener(
					HttpRequest.get("https://api.stopmodreposts.org/minecraft/sites.json")
							.userAgent("HammerLib")
							.body())
					.nextValueARR()
					.stream()
					.flatMap(array ->
							IntStream.range(0, array.size())
									.mapToObj(array::getJSONObject)
									.map(IllegalSite::new))
					.toList();
		} catch(Throwable e)
		{
			e.printStackTrace();
		}
		
		ILLEGAL_SITES = illegalSites;
	}
}