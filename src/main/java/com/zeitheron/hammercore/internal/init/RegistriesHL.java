package com.zeitheron.hammercore.internal.init;


import com.zeitheron.hammercore.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.*;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;

@Mod.EventBusSubscriber
public class RegistriesHL
{
	private static IForgeRegistry<IObjectSourceType> OBJECT_SOURCE;
	
	@SubscribeEvent
	public static void registryEvent(RegistryEvent.NewRegistry e)
	{
		HammerCore.LOG.info("Creating new Forge Registries.");
		
		OBJECT_SOURCE = new RegistryBuilder<IObjectSourceType>()
				.setType(IObjectSourceType.class)
				.setName(HLConstants.id("obj_sources"))
				.disableSaving()
				.create();
	}

	public static IForgeRegistry<IObjectSourceType> OBJECT_SOURCE()
	{
		return OBJECT_SOURCE;
	}
}