package org.zeith.hammerlib.api.fml;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import org.zeith.hammerlib.api.items.IIngredientProvider;

import java.util.ArrayList;

public class ItemProviderArrayList<T extends IItemProvider>
		extends ArrayList<T>
		implements IIngredientProvider
{
	@Override
	public Ingredient asIngredient()
	{
		return Ingredient.of(toArray(new IItemProvider[size()]));
	}
}