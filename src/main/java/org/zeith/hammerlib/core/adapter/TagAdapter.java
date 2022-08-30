package org.zeith.hammerlib.core.adapter;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.BuildTagsEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TagAdapter
{
	static final Map<TagKey<Block>, Set<Block>> blockTags = new ConcurrentHashMap<>();
	static final Map<TagKey<Item>, Set<Item>> itemTags = new ConcurrentHashMap<>();
	
	static
	{
		HammerLib.EVENT_BUS.addListener(TagAdapter::applyTags);
	}
	
	public static void bindStaticTag(TagKey<Block> tag, Block... blocks)
	{
		Set<Block> set = TagAdapter.blockTags.computeIfAbsent(tag, b -> new HashSet<>());
		set.addAll(List.of(blocks));
	}
	
	public static void bindStaticTag(TagKey<Item> tag, Item... blocks)
	{
		Set<Item> set = TagAdapter.itemTags.computeIfAbsent(tag, b -> new HashSet<>());
		set.addAll(List.of(blocks));
	}
	
	public static void applyTags(BuildTagsEvent evt)
	{
		if(evt.reg == ForgeRegistries.BLOCKS)
			blockTags.forEach(evt::addAllToTag);
		if(evt.reg == ForgeRegistries.ITEMS)
			itemTags.forEach(evt::addAllToTag);
	}
}