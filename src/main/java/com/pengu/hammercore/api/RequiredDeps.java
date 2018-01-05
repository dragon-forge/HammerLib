package com.pengu.hammercore.api;

import java.util.HashMap;
import java.util.Map;

import com.pengu.hammercore.event.GetAllRequiredApisEvent;

/**
 * Gather all required {@link iHammerCoreAPI}s
 */
public class RequiredDeps
{
	private static final Map<String, String> REQUESTED_DEPS = new HashMap<>();
	
	public static void addRequests(GetAllRequiredApisEvent evt)
	{
		REQUESTED_DEPS.putAll(evt.NDEP);
	}
	
	public static void addRequest(String api, String version)
	{
		REQUESTED_DEPS.put(api, "=" + version);
	}
	
	public static void addRequest(String api)
	{
		REQUESTED_DEPS.put(api, "~");
	}
	
	public static boolean allDepsResolved()
	{
		for(String api : REQUESTED_DEPS.keySet())
		{
			String version = REQUESTED_DEPS.get(api);
			
			if(version != null)
			{
				if(version.equals("~"))
					version = null;
				else if(version.startsWith("="))
					version = version.substring(1);
			}
			
			boolean resolved = version != null ? APILoader.isApiLoaded(api, version) : APILoader.isApiLoaded(api);
			if(!resolved)
				return false;
		}
		
		return true;
	}
	
	public static Map<String, String> getAllMissingDeps()
	{
		Map<String, String> MISSINGS = new HashMap<>();
		for(String api : REQUESTED_DEPS.keySet())
		{
			String version = REQUESTED_DEPS.get(api);
			
			if(version != null)
			{
				if(version.equals("~"))
					version = null;
				else if(version.startsWith("="))
					version = version.substring(1);
			}
			
			boolean resolved = version != null ? APILoader.isApiLoaded(api, version) : APILoader.isApiLoaded(api);
			if(!resolved)
				MISSINGS.put(api, version);
		}
		return MISSINGS;
	}
}