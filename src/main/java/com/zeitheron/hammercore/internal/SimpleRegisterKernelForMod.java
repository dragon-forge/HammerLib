package com.zeitheron.hammercore.internal;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class SimpleRegisterKernelForMod
		extends ArrayList<SimpleRegisterKernel>
{
	protected final ModContainer container;
	
	public SimpleRegisterKernelForMod(ModContainer container)
	{
		this.container = container;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	protected void register(RegistryEvent.Register evt)
	{
		ModContainer old = Loader.instance().activeModContainer();
		Loader.instance().setActiveModContainer(container);
		evt.setModContainer(container);
		for(SimpleRegisterKernel k : this)
			k.register(evt);
		Loader.instance().setActiveModContainer(old);
	}
	
	public String getModId()
	{
		return container.getModId();
	}
}