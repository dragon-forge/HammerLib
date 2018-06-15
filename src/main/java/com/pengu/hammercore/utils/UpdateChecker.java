package com.pengu.hammercore.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.VersionCompareTool;
import com.pengu.hammercore.common.utils.VersionCompareTool.EnumVersionLevel;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONObject;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ComparableVersion;

public class UpdateChecker
{
	private static final Map<String, ModVersion> stable = new HashMap<>();
	private static final Map<String, ModVersion> latest = new HashMap<>();
	
	/** Read more about this at https://pastebin.com/y9q65j8f */
	public static final List<String> updateFiles = new ArrayList<>();
	
	public static void refresh()
	{
		stable.clear();
		latest.clear();
		
		List<String> jsons = new ArrayList<>(updateFiles);
		jsons.add("https://pastebin.com/raw/BzzpKGUe");
		
		HammerCore.LOG.info("Fetching remote versions...");
		
		for(String url : jsons)
			try
			{
				JSONObject jobj = (JSONObject) IOUtils.downloadjson(url);
				jobj = jobj.getJSONObject(Loader.MC_VERSION);
				for(String modid : jobj.keySet())
				{
					JSONObject jo = jobj.getJSONObject(modid);
					JSONObject sta = jo.getJSONObject("stable");
					JSONObject lat = jo.getJSONObject("latest");
					stable.put(modid, new ModVersion(modid, sta.getString("index"), sta.getString("url")));
					latest.put(modid, new ModVersion(modid, lat.getString("index"), lat.getString("url")));
				}
			} catch(JSONException e)
			{
				e.printStackTrace();
			}
		
		HammerCore.LOG.info("Loaded " + latest.size() + " mods with version tracking.");
		
		installedUpdateableMods().forEach(modid ->
		{
			boolean uptodate = true;
			
			ModVersion ver = getLatestVersion(modid), latest = ver;
			if(ver != null && ver.check() == EnumVersionLevel.NEWER)
			{
				uptodate = false;
				HammerCore.LOG.info("New latest version for mod " + ver.getModName() + " detected: " + ver.remVer + ". Download: " + ver.url);
			}
			
			ver = getStableVersion(modid);
			if(ver != null && ver.check() == EnumVersionLevel.NEWER)
			{
				uptodate = false;
				HammerCore.LOG.info("New stable version for mod " + ver.getModName() + " detected: " + ver.remVer + ". Download: " + ver.url);
			}
			
			boolean beta = ver != null && latest != null && latest.compare(ver) == EnumVersionLevel.NEWER;
			ModVersion actual = beta ? latest : ver;
			
			boolean ahead = actual.check() == EnumVersionLevel.OLDER;
			
			ModContainer mc = Loader.instance().getIndexedModList().get(modid);
			if(mc != null)
				try
				{
					Field field = ForgeVersion.class.getDeclaredField("results");
					field.setAccessible(true);
					Map<ModContainer, CheckResult> results = (Map<ModContainer, CheckResult>) field.get(null);
					results.put(mc, newCR(ahead ? Status.AHEAD : uptodate ? Status.UP_TO_DATE : beta ? Status.BETA_OUTDATED : Status.OUTDATED, new ComparableVersion(actual.remVer), null, actual.url));
				} catch(Throwable err)
				{
				}
		});
	}
	
	private static CheckResult newCR(Status status, @Nullable ComparableVersion target, @Nullable Map<ComparableVersion, String> changes, @Nullable String url) throws ReflectiveOperationException, RuntimeException
	{
		Constructor<CheckResult> crc = CheckResult.class.getDeclaredConstructor(Status.class, ComparableVersion.class, Map.class, String.class);
		crc.setAccessible(true);
		return crc.newInstance(status, target, changes, url);
	}
	
	public static Stream<String> installedUpdateableMods()
	{
		return latest.keySet().stream().filter(Loader::isModLoaded);
	}
	
	@Nullable
	public static ModVersion getLatestVersion(String modid)
	{
		return latest.get(modid);
	}
	
	@Nullable
	public static ModVersion getStableVersion(String modid)
	{
		return stable.get(modid);
	}
	
	public static class ModVersion
	{
		public final String mod;
		public final String remVer;
		public final String url;
		
		public ModVersion(String mod, String ver, String url)
		{
			this.mod = mod;
			this.remVer = ver;
			this.url = url;
		}
		
		public EnumVersionLevel compare(ModVersion ver)
		{
			return new VersionCompareTool(remVer).compare(new VersionCompareTool(ver.remVer));
		}
		
		public EnumVersionLevel check()
		{
			if(Loader.isModLoaded(mod))
			{
				ModContainer mc = Loader.instance().getIndexedModList().get(mod);
				String ver = mc != null ? mc.getVersion() : "?";
				return new VersionCompareTool(remVer).compare(new VersionCompareTool(ver));
			} else
				return EnumVersionLevel.SAME;
		}
		
		public String getModName()
		{
			ModContainer mc = Loader.instance().getIndexedModList().get(mod);
			if(mc != null)
				return mc.getName();
			return mod;
		}
	}
}