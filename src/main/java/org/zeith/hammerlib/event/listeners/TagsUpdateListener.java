package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.zeith.hammerlib.util.SidedLocal;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@EventBusSubscriber
public class TagsUpdateListener
{
	private static SidedLocal<RegistryAccess> registryAccess = SidedLocal.initializeForBoth(() -> RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
	private static Map<ResourceKey<? extends Registry>, Map> tagCache = new HashMap<>();
	
	public static final ICondition.IContext REMOTE_TAG_ACCESS = new ICondition.IContext()
	{
		@Override
		public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry)
		{
			if(tagCache.containsKey(registry))
				return tagCache.get(registry);
			
			Map<ResourceLocation, Collection<Holder<T>>> gen = registryAccess
					.get()
					.registry(registry)
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
	
	public static RegistryAccess getRegistryAccess()
	{
		return registryAccess.get();
	}
	
	public static RegistryAccess getRegistryAccess(LogicalSide side)
	{
		return registryAccess.get(side);
	}
	
	@SubscribeEvent
	public static void receiveClientsideTags(TagsUpdatedEvent e)
	{
		if(e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED)
		{
			registryAccess.set(LogicalSide.CLIENT, e.getRegistryAccess());
			tagCache.clear();
		} else if(e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD)
		{
			registryAccess.set(LogicalSide.SERVER, e.getRegistryAccess());
		}
	}
}