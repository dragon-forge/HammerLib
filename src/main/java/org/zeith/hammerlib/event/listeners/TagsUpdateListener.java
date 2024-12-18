package org.zeith.hammerlib.event.listeners;

import lombok.Getter;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@EventBusSubscriber
public class TagsUpdateListener
{
	@Getter
	private static HolderLookup.Provider registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	private static Map<ResourceKey<? extends Registry>, Map> tagCache = new HashMap<>();
	
	public static <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry)
	{
		if(tagCache.containsKey(registry))
			return tagCache.get(registry);
		
		Map<ResourceLocation, Collection<Holder<T>>> gen = registryAccess.lookup(registry)
				.map(HolderLookup::listTags)
				.stream()
				.flatMap(UnaryOperator.identity())
				.collect(Collectors.toMap(
								tag -> tag.key().location(),
								tag -> tag.stream().toList()
						)
				);
		
		tagCache.put(registry, gen);
		
		return gen;
	}
	
	@SubscribeEvent
	public static void receiveClientsideTags(TagsUpdatedEvent e)
	{
		if(e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED)
		{
			registryAccess = e.getLookupProvider();
			tagCache.clear();
		}
	}
}