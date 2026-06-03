package com.zeitheron.hammercore.internal;

import net.minecraft.profiler.Profiler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class SimpleRegisterKernelForMod
		extends ArrayList<SimpleRegisterKernel>
{
	protected final ModContainer container;
	protected final Profiler profiler = new Profiler();
	
	public SimpleRegisterKernelForMod(ModContainer container)
	{
		this.profiler.profilingEnabled = true;
		this.container = container;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	protected void register(RegistryEvent.Register evt)
	{
		profiler.startSection("registerClasses." + evt.getRegistry().getRegistrySuperType().getName());
		ModContainer old = Loader.instance().activeModContainer();
		Loader.instance().setActiveModContainer(container);
		evt.setModContainer(container);
		for(SimpleRegisterKernel k : this)
			k.register(evt);
		Loader.instance().setActiveModContainer(old);
		profiler.endSection();
	}
	
	public String getModId()
	{
		return container.getModId();
	}
}