package org.zeith.hammerlib.api.crafting.impl;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import org.zeith.hammerlib.core.RecipeHelper;

public class TagIngredient
		extends MCIngredient
{
	public final ITag.INamedTag<Item> tag;

	public TagIngredient(ITag.INamedTag<Item> tag)
	{
		super(RecipeHelper.fromComponent(tag));
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
		public final ITag.INamedTag<Item> tag;

		public TagQIngredient(ITag.INamedTag<Item> tag, int count)
		{
			super(RecipeHelper.fromComponent(tag), count);
			this.tag = tag;
		}

		@Override
		public IQuantifiableIngredient<?> quantify(int count)
		{
			return new TagQIngredient(tag, this.count * count);
		}
	}
}