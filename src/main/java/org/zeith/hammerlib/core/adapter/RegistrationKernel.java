package org.zeith.hammerlib.core.adapter;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.util.Objects;

public class RegistrationKernel
{
	protected final ScanDataHelper.ModAwareAnnotationData data;
	protected final FMLModContainer mc;
	protected final String prefix;
	
	public RegistrationKernel(ScanDataHelper.ModAwareAnnotationData data, FMLModContainer mc)
	{
		this.data = data;
		this.mc = mc;
		this.prefix = data.getProperty("prefix").map(Objects::toString).orElse("");
	}
	
	@SubscribeEvent
	public void register(RegistryEvent.Register event)
	{
		RegistryAdapter.register(event, data.getOwnerClass(), mc.getModId(), prefix);
	}
}