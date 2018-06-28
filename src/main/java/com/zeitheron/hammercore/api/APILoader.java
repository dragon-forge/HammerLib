package com.zeitheron.hammercore.api;

import com.zeitheron.hammercore.HammerCore;

import net.minecraftforge.fml.common.versioning.ComparableVersion;

/**
 * Allows modder to check for external APIs
 */
public class APILoader
{
	public static boolean isApiLoaded(String api)
	{
		String version = getApiVersion(api);
		return version != null;
	}
	
	public static boolean isApiLoaded(String api, String minVersion)
	{
		String version = getApiVersion(api);
		if(version == null)
			return false;
		return new ComparableVersion(version).compareTo(new ComparableVersion(minVersion)) >= 0;
	}
	
	public static String getApiVersion(String api)
	{
		for(IHammerCoreAPI iapi : HammerCore.APIS.keySet())
			if(HammerCore.APIS.get(iapi).name().equals(api))
				return HammerCore.APIS.get(iapi).version();
		return null;
	}
}