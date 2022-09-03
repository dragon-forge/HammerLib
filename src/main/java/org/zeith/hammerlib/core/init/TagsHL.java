package org.zeith.hammerlib.core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

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
		
		public static final TagKey<Item> PISTONS = tag("pistons");
		public static final TagKey<Item> STORAGE_BLOCKS_GLOWSTONE = tag("storage_blocks/glowstone");
		public static final TagKey<Item> TOOLS_WRENCH = tag("tools/wrenc");
		
		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}
}