package org.zeith.hammerlib.util.mcf;

import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.zeith.hammerlib.util.java.ReflectionUtil;

public class BusHelper
{
	public static EnumEventState postSafe(IEventBus bus, Event evt)
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

	public static EnumEventState post(IEventBus bus, Event evt)
	{
		return bus.post(evt) ? EnumEventState.CLOSED : EnumEventState.DISPATCHED;
	}

	public static void setShutdownState(IEventBus bus, boolean shutdown)
	{
		try
		{
			ReflectionUtil.lookupField(EventBus.class, "shutdown").setBoolean(bus, shutdown);
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