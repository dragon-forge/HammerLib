package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public class ShapelessRecipeBuilder
		extends RecipeBuilder<ShapelessRecipeBuilder>
{
	private final NonNullList<Ingredient> ingredients = NonNullList.create();

	public ShapelessRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	public ShapelessRecipeBuilder add(Object ingredient)
	{
		this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}

	public ShapelessRecipeBuilder addAll(Object... ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}

	public ShapelessRecipeBuilder addAll(Iterable<Object> ingredients)
	{
		for(Object ingredient : ingredients) this.ingredients.add(RecipeHelper.fromComponent(ingredient));
		return this;
	}

	@Override
	public void register()
	{
		validate();
		if(ingredients.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredients!");
		event.add(new ShapelessRecipe(getIdentifier(), group, result, ingredients));
	}
}