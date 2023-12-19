package org.zeith.hammerlib.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.*;
import org.zeith.api.registry.RegistryMapping;
import org.zeith.hammerlib.abstractions.sources.IObjectSourceType;
import org.zeith.hammerlib.core.recipes.replacers.IRemainingItemReplacer;
import org.zeith.hammerlib.proxy.HLConstants;

import static net.minecraft.resources.ResourceKey.createRegistryKey;
import static org.zeith.hammerlib.proxy.HLConstants.id;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistriesHL
{
	private static Registry<IObjectSourceType> OBJECT_SOURCE;
	private static Registry<IRemainingItemReplacer> REMAINING_REPLACER;
	
	@SubscribeEvent
	public static void newRegistries(NewRegistryEvent e)
	{
		OBJECT_SOURCE = e.create(new RegistryBuilder<>(Keys.OBJECT_SOURCE).sync(false));
		RegistryMapping.report(IObjectSourceType.class, OBJECT_SOURCE, false);
		
		REMAINING_REPLACER = e.create(new RegistryBuilder<>(Keys.REMAINING_ITEM_REPLACER).sync(false).defaultKey(HLConstants.id("none")));
		RegistryMapping.report(IRemainingItemReplacer.class, REMAINING_REPLACER, false);
	}
	
	public static Registry<IObjectSourceType> objectSources()
	{
		return OBJECT_SOURCE;
	}
	
	public static Registry<IRemainingItemReplacer> remainingReplacer()
	{
		return REMAINING_REPLACER;
	}
	
	public interface Keys
	{
		ResourceKey<? extends Registry<IRemainingItemReplacer>> REMAINING_ITEM_REPLACER = createRegistryKey(id("recipe_replacer"));
		ResourceKey<? extends Registry<IObjectSourceType>> OBJECT_SOURCE = createRegistryKey(id("obj_sources"));
	}
}