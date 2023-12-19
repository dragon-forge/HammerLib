package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TagsUpdateListener
{
	private static RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	private static Map<ResourceKey<? extends Registry>, Map> tagCache = new HashMap<>();
	
	public static final ICondition.IContext REMOTE_TAG_ACCESS = new ICondition.IContext()
	{
		@Override
		public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry)
		{
			if(tagCache.containsKey(registry))
				return tagCache.get(registry);
			
			Map<ResourceLocation, Collection<Holder<T>>> gen = registryAccess.registry(registry)
					.map(Registry::getTags)
					.stream()
					.flatMap(UnaryOperator.identity())
					.collect(Collectors.toMap(
									tag -> tag.getFirst().location(),
									tag -> tag.getSecond().stream().toList()
							)
					);
			
			tagCache.put(registry, gen);
			
			return gen;
		}
	};
	
	@SubscribeEvent
	public static void receiveClientsideTags(TagsUpdatedEvent e)
	{
		if(e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED)
		{
			registryAccess = e.getRegistryAccess();
			tagCache.clear();
		}
	}
}