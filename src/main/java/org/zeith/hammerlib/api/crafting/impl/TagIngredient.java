package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.zeith.hammerlib.core.RecipeHelper;

public class TagIngredient
		extends MCIngredient
{
	public final TagKey<Item> tag;

	public TagIngredient(TagKey<Item> tag)
	{
		super(() -> RecipeHelper.fromComponent(tag));
		this.tag = tag;
	}

	@Override
	public IQuantifiableIngredient<?> quantify(int count)
	{
		return new TagQIngredient(tag, count);
	}

	public static class TagQIngredient
			extends MCQIngredient
	{
		public final TagKey<Item> tag;

		public TagQIngredient(TagKey<Item> tag, int count)
		{
			super(() -> RecipeHelper.fromComponent(tag), count);
			this.tag = tag;
		}

		@Override
		public IQuantifiableIngredient<?> quantify(int count)
		{
			return new TagQIngredient(tag, this.count * count);
		}
	}
}