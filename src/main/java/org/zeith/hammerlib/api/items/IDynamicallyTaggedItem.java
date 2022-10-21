package org.zeith.hammerlib.api.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public interface IDynamicallyTaggedItem
{
	Stream<TagKey<Item>> getExtraItemTags(ItemStack stack);
	
	default boolean is(ItemStack stack, Item thisItem, TagKey<Item> tag)
	{
		return thisItem.builtInRegistryHolder().is(tag)
				|| getExtraItemTags(stack).anyMatch(tag::equals);
	}
	
	default Stream<TagKey<Item>> tags(ItemStack stack, Item thisItem)
	{
		return Stream.concat(thisItem.builtInRegistryHolder().tags(), getExtraItemTags(stack));
	}
}