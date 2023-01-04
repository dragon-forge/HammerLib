package org.zeith.hammerlib.core.adapter;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.zeith.hammerlib.util.java.io.win32.ZoneIdentifier;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;

import java.io.File;
import java.net.*;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
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
		return Optional.ofNullable(ModList.get().getModFileById(modId))
				.map(ModFileInfo::getFile)
				.map(ModFile::getFilePath)
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
			if(modFile == null) modFile = pathOf(modClass.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
			return ZoneIdentifier.forFile(modFile).map(ModSource::new);
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	public static Path pathOf(URI uri)
	{
		String scheme = uri.getScheme();
		if(scheme == null)
			throw new IllegalArgumentException("Missing scheme");
		
		// check for default provider to avoid loading of installed providers
		if(scheme.equalsIgnoreCase("file"))
			return FileSystems.getDefault().provider().getPath(uri);
		
		// try to find provider
		for(FileSystemProvider provider : FileSystemProvider.installedProviders())
		{
			if(provider.getScheme().equalsIgnoreCase(scheme))
			{
				return provider.getPath(uri);
			}
		}
		
		throw new FileSystemNotFoundException("Provider \"" + scheme + "\" not installed");
	}
	
	public static final class ModSource
	{
		private final String referrerUrl;
		private final String hostUrl;
		
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
		
		public String referrerUrl()
		{
			return referrerUrl;
		}
		
		public String hostUrl()
		{
			return hostUrl;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this) return true;
			if(obj == null || obj.getClass() != this.getClass()) return false;
			ModSource that = (ModSource) obj;
			return Objects.equals(this.referrerUrl, that.referrerUrl) &&
					Objects.equals(this.hostUrl, that.hostUrl);
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(referrerUrl, hostUrl);
		}
		
		@Override
		public String toString()
		{
			return "ModSource[" +
					"referrerUrl=" + referrerUrl + ", " +
					"hostUrl=" + hostUrl + ']';
		}
		
	}
	
	public static final class IllegalSite
			implements Predicate<URL>
	{
		private final String domain;
		private final String notes;
		private final String path;
		private final String reason;
		
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
		
		public String domain()
		{
			return domain;
		}
		
		public String notes()
		{
			return notes;
		}
		
		public String path()
		{
			return path;
		}
		
		public String reason()
		{
			return reason;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this) return true;
			if(obj == null || obj.getClass() != this.getClass()) return false;
			IllegalSite that = (IllegalSite) obj;
			return Objects.equals(this.domain, that.domain) &&
					Objects.equals(this.notes, that.notes) &&
					Objects.equals(this.path, that.path) &&
					Objects.equals(this.reason, that.reason);
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(domain, notes, path, reason);
		}
		
		@Override
		public String toString()
		{
			return "IllegalSite[" +
					"domain=" + domain + ", " +
					"notes=" + notes + ", " +
					"path=" + path + ", " +
					"reason=" + reason + ']';
		}
	}
	
	static
	{
		List<IllegalSite> illegalSites = Collections.emptyList();
		
		try
		{
			illegalSites = Stream.of(new JSONTokener(
							HttpRequest.get("https://api.stopmodreposts.org/minecraft/sites.json")
									.userAgent("HammerLib")
									.body())
							.nextValueARR().orElse(null)).filter(Objects::nonNull)
					.flatMap(array ->
							IntStream.range(0, array.length())
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