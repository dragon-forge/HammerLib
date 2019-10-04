package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ProfilerEndEvent extends Event
{
	public static final ProfilerEndEvent INSTANCE = new ProfilerEndEvent();
	
	private ProfilerEndEvent()
	{
	}
	
	@Override
	public boolean isCancelable()
	{
		return false;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.PROFILER_END);
	}
}