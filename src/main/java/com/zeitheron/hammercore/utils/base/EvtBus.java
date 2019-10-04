package com.zeitheron.hammercore.utils.base;

import com.zeitheron.hammercore.utils.ReflectionUtil;

import net.minecraftforge.fml.common.eventhandler.Event;

import net.minecraftforge.fml.common.eventhandler.EventBus;

public class EvtBus
{
	public static EnumEventState postSafe(EventBus bus, Event evt)
	{
		try
		{
			return post(bus, evt);
		} catch(Throwable err)
		{
			err.printStackTrace();
			return EnumEventState.ERRORED;
		}
	}
	
	public static EnumEventState post(EventBus bus, Event evt)
	{
		return bus.post(evt) ? EnumEventState.CLOSED : EnumEventState.DISPATCHED;
	}
	
	public static void setShutdownState(EventBus bus, boolean shutdown)
	{
		try
		{
			ReflectionUtil.getField(EventBus.class, "shutdown").setBoolean(bus, shutdown);
		} catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public enum EnumEventState
	{
		CLOSED, //
		DISPATCHED, //
		ERRORED, //
		UNHANDLED;
		
		public boolean isClosed()
		{
			return this == CLOSED;
		}
		
		public boolean isOpen()
		{
			return this == DISPATCHED;
		}
		
		public boolean hasErrored()
		{
			return this == ERRORED;
		}
	}
}