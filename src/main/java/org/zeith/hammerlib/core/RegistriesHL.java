package org.zeith.hammerlib.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.abstractions.actions.ILevelActionType;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;
import org.zeith.hammerlib.proxy.HLConstants;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	private static Registry<IObjectSourceType> OBJECT_SOURCES;
	private static Registry<IRemainingItemReplacer> REMAINING_REPLACER;
	private static Registry<ILevelActionType> LEVEL_ACTIONS;
	
	@SubscribeEvent
	public static void newRegistries(NewRegistryEvent e)
	{
		OBJECT_SOURCES = e.create(new RegistryBuilder<>(Keys.OBJECT_SOURCES).sync(false));
		RegistryMapping.report(IObjectSourceType.class, OBJECT_SOURCES, false);
		
		REMAINING_REPLACER = e.create(new RegistryBuilder<>(Keys.REMAINING_ITEM_REPLACER).sync(false).defaultKey(HLConstants.id("none")));
		RegistryMapping.report(IRemainingItemReplacer.class, REMAINING_REPLACER, false);
		
		LEVEL_ACTIONS = e.create(new RegistryBuilder<>(Keys.LEVEL_ACTIONS).sync(false));
		RegistryMapping.report(ILevelActionType.class, LEVEL_ACTIONS, false);
	}
	
	public static Registry<IObjectSourceType> objectSources()
	{
		return OBJECT_SOURCES;
	}
	
	public static Registry<IRemainingItemReplacer> remainingReplacer()
	{
		return REMAINING_REPLACER;
	}
	
	public static Registry<ILevelActionType> levelActions()
	{
		return LEVEL_ACTIONS;
	}
	
	public static class Keys
	{
		public static final ResourceKey<Registry<IObjectSourceType>> OBJECT_SOURCES = key("obj_sources");
		public static final ResourceKey<Registry<IRemainingItemReplacer>> REMAINING_ITEM_REPLACER = key("recipe_replacer");
		public static final ResourceKey<Registry<ILevelActionType>> LEVEL_ACTIONS = key("level_actions");
		
		private static <T> ResourceKey<Registry<T>> key(String name)
		{
			return ResourceKey.createRegistryKey(HLConstants.id(name));
		}
	}
}