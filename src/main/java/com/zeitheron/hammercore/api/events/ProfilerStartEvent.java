package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ProfilerStartEvent extends Event
{
	private final String section;
	
	public ProfilerStartEvent(String section)
	{
		super();
		this.section = section;
	}
	
	public String getSection()
	{
		return section;
	}
	
	@Override
	public boolean isCancelable()
	{
		return false;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.PROFILER_START);
	}
}