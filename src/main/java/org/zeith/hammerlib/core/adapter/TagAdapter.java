package org.zeith.hammerlib.core.adapter;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TagAdapter
{
	static final Map<TagKey<Block>, Set<Block>> blockTags = new ConcurrentHashMap<>();
	static final Map<TagKey<Item>, Set<Item>> itemTags = new ConcurrentHashMap<>();
	static final Map<TagKey<Fluid>, Set<Fluid>> fluidTags = new ConcurrentHashMap<>();
	
	static final Map<ResourceKey<? extends Registry<?>>, Map<TagKey<?>, Set<?>>> staticTags = new ConcurrentHashMap<>();
	
	static
	{
		HammerLib.EVENT_BUS.addListener(TagAdapter::applyTags);
	}
	
	private static <T> Map<TagKey<T>, Set<T>> getTagsFor(ResourceKey<? extends Registry<T>> registry)
	{
		return Cast.cast(staticTags.computeIfAbsent(registry, r -> new ConcurrentHashMap<>()));
	}
	
	public static synchronized <T> void bind(TagKey<T> tag, T... values)
	{
		var tags = getTagsFor(tag.registry());
		tags.computeIfAbsent(tag, b -> new HashSet<>()).addAll(List.of(values));
	}
	
	@SuppressWarnings({
			"rawtypes",
			"Convert2MethodRef"
	})
	public static void applyTags(BuildTagsEvent evt)
	{
		Map<TagKey, Set> tags = getTagsFor(evt.reg.getRegistryKey());
		tags.forEach((tag, values) -> evt.addAllToTag(tag, values));
	}
}