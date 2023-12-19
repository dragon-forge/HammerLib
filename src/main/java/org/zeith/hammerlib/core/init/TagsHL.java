package org.zeith.hammerlib.core.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.zeith.hammerlib.core.RecipeHelper;

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
		public static final TagKey<Item> TOOLS_WRENCH = tag("tools/wrench");
		
		public static final TagKey<Item> GEARS_WOODEN = tag("gears/wooden");
		public static final TagKey<Item> GEARS_STONE = tag("gears/stone");
		public static final TagKey<Item> GEARS_COPPER = tag("gears/copper");
		public static final TagKey<Item> GEARS_IRON = tag("gears/iron");
		public static final TagKey<Item> GEARS_GOLD = tag("gears/gold");
		public static final TagKey<Item> GEARS_DIAMOND = tag("gears/diamond");
		public static final TagKey<Item> GEARS_NETHERITE = tag("gears/netherite");
		
		private static TagKey<Item> tag(String name)
		{
			return ItemTags.create(new ResourceLocation(RecipeHelper.NEOFORGE_MOD_ID_FOR_TAGS, name));
		}
	}
}