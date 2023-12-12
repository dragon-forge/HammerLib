package org.zeith.hammerlib.core.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
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
		public static final Tags.IOptionalNamedTag<Item> TOOLS_WRENCH = tag("tools/wrench");
		
		public static final Tags.IOptionalNamedTag<Item> GEARS_WOODEN = tag("gears/wooden");
		public static final Tags.IOptionalNamedTag<Item> GEARS_STONE = tag("gears/stone");
		public static final Tags.IOptionalNamedTag<Item> GEARS_COPPER = tag("gears/copper");
		public static final Tags.IOptionalNamedTag<Item> GEARS_IRON = tag("gears/iron");
		public static final Tags.IOptionalNamedTag<Item> GEARS_GOLD = tag("gears/gold");
		public static final Tags.IOptionalNamedTag<Item> GEARS_DIAMOND = tag("gears/diamond");
		public static final Tags.IOptionalNamedTag<Item> GEARS_NETHERITE = tag("gears/netherite");

		private static Tags.IOptionalNamedTag<Item> tag(String name)
		{
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}
	}
}