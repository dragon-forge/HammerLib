package org.zeith.hammerlib.core;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	private static Supplier<IForgeRegistry<IObjectSourceType>> OBJECT_SOURCES;
	
	@SubscribeEvent
	public static void newRegistries(NewRegistryEvent e)
	{
		OBJECT_SOURCES = e.create(new RegistryBuilder<IObjectSourceType>()
						.setName(HammerLib.id("obj_sources"))
						.disableSaving()
						.disableSaving(),
				reg -> RegistryMapping.report(IObjectSourceType.class, reg, false)
		);
	}
	
	public static IForgeRegistry<IObjectSourceType> animationSources()
	{
		return OBJECT_SOURCES.get();
	}
}