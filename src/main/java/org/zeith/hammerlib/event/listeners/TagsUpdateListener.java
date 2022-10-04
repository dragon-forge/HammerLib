package org.zeith.hammerlib.event.listeners;

import net.minecraft.core.*;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.api.crafting.AbstractRecipeRegistry;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TagsUpdateListener
{
	private static RegistryAccess registryAccess = BuiltinRegistries.ACCESS;
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
			
			AbstractRecipeRegistry.getAllRegistries()
					.forEach(AbstractRecipeRegistry::addClientsideContents);
		}
	}
}