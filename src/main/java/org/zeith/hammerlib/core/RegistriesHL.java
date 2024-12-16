package org.zeith.hammerlib.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.zeith.api.registry.MappedRegistryBuilder;
import org.zeith.hammerlib.abstractions.actions.ILevelActionType;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;
import org.zeith.hammerlib.api.items.glint.IGlintProviderType;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;
import org.zeith.hammerlib.proxy.HLConstants;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	public static final Registry<IObjectSourceType> OBJECT_SOURCES = new MappedRegistryBuilder<>(IObjectSourceType.class, Keys.OBJECT_SOURCES).sync(false).create();
	public static final Registry<IRemainingItemReplacer> REMAINING_REPLACER = new MappedRegistryBuilder<>(IRemainingItemReplacer.class, Keys.REMAINING_ITEM_REPLACER).sync(false).defaultKey(HLConstants.id("none")).create();
	public static final Registry<ILevelActionType> LEVEL_ACTIONS = new MappedRegistryBuilder<>(ILevelActionType.class, Keys.LEVEL_ACTIONS).sync(false).create();
	public static final Registry<IGlintProviderType<?>> GLINT_PROVIDERS = new MappedRegistryBuilder<>(IGlintProviderType.class, Keys.GLINT_PROVIDERS).sync(false).create();
	
	@SubscribeEvent
	public static void newRegistries(NewRegistryEvent e)
	{
		e.register(OBJECT_SOURCES);
		e.register(REMAINING_REPLACER);
		e.register(LEVEL_ACTIONS);
		e.register(GLINT_PROVIDERS);
	}
	
	public static class Keys
	{
		public static final ResourceKey<Registry<IObjectSourceType>> OBJECT_SOURCES = key("obj_sources");
		public static final ResourceKey<Registry<IRemainingItemReplacer>> REMAINING_ITEM_REPLACER = key("recipe_replacer");
		public static final ResourceKey<Registry<ILevelActionType>> LEVEL_ACTIONS = key("level_actions");
		public static final ResourceKey<Registry<IGlintProviderType<?>>> GLINT_PROVIDERS = key("glint_providers");
		
		private static <T> ResourceKey<Registry<T>> key(String name)
		{
			return ResourceKey.createRegistryKey(HLConstants.id(name));
		}
	}
}