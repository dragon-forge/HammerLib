package org.zeith.hammerlib.api.fml;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.api.items.IIngredientProvider;

import java.util.ArrayList;

public class ItemLikeArrayList<T extends ItemLike>
		extends ArrayList<T>
		implements IIngredientProvider
{
	@Override
	public Ingredient asIngredient()
	{
		return Ingredient.of(toArray(new ItemLike[size()]));
	}
}