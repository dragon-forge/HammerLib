package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.asm.McHooks;
import com.zeitheron.hammercore.asm.McHooks.EnumMcHook;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DisableLightingEvent extends Event
{
	public static final DisableLightingEvent INSTANCE = new DisableLightingEvent();
	
	private DisableLightingEvent()
	{
	}
	
	@Override
	public boolean isCancelable()
	{
		return false;
	}
	
	public static void enable()
	{
		McHooks.enableFeature(EnumMcHook.DISABLE_LIGHTING);
	}
}