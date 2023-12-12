package org.zeith.hammerlib.core;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	private static Supplier<IForgeRegistry<IObjectSourceType>> OBJECT_SOURCES;
	
	@SubscribeEvent
	public static void newRegistries(RegistryEvent.NewRegistry e)
	{
		OBJECT_SOURCES = Cast.constant(new RegistryBuilder<IObjectSourceType>()
				.setType(IObjectSourceType.class)
				.setName(HLConstants.id("obj_sources"))
				.disableSaving()
				.disableSaving()
				.create());
	}
	
	public static IForgeRegistry<IObjectSourceType> animationSources()
	{
		return OBJECT_SOURCES.get();
	}
}