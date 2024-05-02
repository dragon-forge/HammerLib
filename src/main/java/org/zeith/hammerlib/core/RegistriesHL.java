package org.zeith.hammerlib.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.abstractions.actions.ILevelActionType;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	private static Supplier<IForgeRegistry<IObjectSourceType>> OBJECT_SOURCES;
	private static Supplier<IForgeRegistry<ILevelActionType>> LEVEL_ACTIONS;
	
	@SubscribeEvent
	public static void newRegistries(NewRegistryEvent e)
	{
		OBJECT_SOURCES = e.create(new RegistryBuilder<IObjectSourceType>()
						.setName(HammerLib.id("obj_sources"))
						.disableSaving(),
				reg -> RegistryMapping.report(IObjectSourceType.class, reg, false)
		);
		
		LEVEL_ACTIONS = e.create(new RegistryBuilder<ILevelActionType>()
						.setName(HammerLib.id("level_actions"))
						.disableSaving(),
				reg -> RegistryMapping.report(ILevelActionType.class, reg, false)
		);
	}
	
	public static IForgeRegistry<IObjectSourceType> animationSources()
	{
		return OBJECT_SOURCES.get();
	}
	
	public static IForgeRegistry<ILevelActionType> levelActions()
	{
		return LEVEL_ACTIONS.get();
	}
	
	public static class Keys
	{
		public static final ResourceKey<Registry<IObjectSourceType>> OBJECT_SOURCES = key("obj_sources");
		public static final ResourceKey<Registry<ILevelActionType>> LEVEL_ACTIONS = key("level_actions");
		
		private static <T> ResourceKey<Registry<T>> key(String name)
		{
			return ResourceKey.createRegistryKey(HammerLib.id(name));
		}
	}
}