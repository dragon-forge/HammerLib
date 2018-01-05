package com.pengu.hammercore.event;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GetAllRequiredApisEvent extends Event
{
	public final Map<String, String> NDEP = new HashMap<>();
	
	public void addRequiredApi(String api)
	{
		NDEP.put(api, "~");
	}
	
	public void addRequiredApi(String api, String version)
	{
		NDEP.put(api, "=" + version);
	}
}