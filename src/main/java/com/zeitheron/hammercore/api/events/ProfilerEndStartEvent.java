package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

public class ProfilerEndStartEvent extends ProfilerStartEvent
{
	public ProfilerEndStartEvent(String section)
	{
		super(section);
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.PROFILER_END_START);
	}
}