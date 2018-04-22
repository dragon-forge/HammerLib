package com.pengu.hammercore.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.pengu.hammercore.common.utils.IOUtils;
import com.pengu.hammercore.common.utils.VersionCompareTool;
import com.pengu.hammercore.common.utils.VersionCompareTool.EnumVersionLevel;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONObject;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class ModVersions
{
	private static final Map<String, ModVersion> stable = new HashMap<>();
	private static final Map<String, ModVersion> latest = new HashMap<>();
	
	public static void refresh()
	{
		stable.clear();
		latest.clear();
		
		try
		{
			JSONObject jobj = (JSONObject) IOUtils.downloadjson("https://pastebin.com/raw/BzzpKGUe");
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