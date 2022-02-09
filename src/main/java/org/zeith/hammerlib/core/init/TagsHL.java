package org.zeith.hammerlib.core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class TagsHL
{
	public static void init()
	{
		Items.init();
	}

	public static class Items
	{
		private static void init()
		{
		}

		public static final Tags.IOptionalNamedTag<Item> PISTONS = tag("pistons");
		public static final Tags.IOptionalNamedTag<Item> STORAGE_BLOCKS_GLOWSTONE = tag("storage_blocks/glowstone");

		private static Tags.IOptionalNamedTag<Item> tag(String name)
		{
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}
	}
}