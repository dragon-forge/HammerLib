package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipeBuilder
		extends RecipeBuilder<ShapedRecipeBuilder>
{
	private final Map<Character, Ingredient> dictionary = new HashMap<>();
	private RecipeShape shape;

	public ShapedRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	public ShapedRecipeBuilder shape(int width, int height, String... shapeKeys)
	{
		this.shape = new RecipeShape(width, height, shapeKeys);
		return this;
	}

	public ShapedRecipeBuilder shape(String... shapeKeys)
	{
		this.shape = new RecipeShape(shapeKeys);
		return this;
	}

	public ShapedRecipeBuilder map(char c, Object ingredient)
	{
		dictionary.put(c, RecipeHelper.fromComponent(ingredient));
		return this;
	}

	@Override
	public void register()
	{
		validate();
		if(shape == null)
			throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined shape!");
		if(dictionary.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredients!");

		StringBuilder s = new StringBuilder();
		for(String s2 : shape.shape) s.append(s2);

		NonNullList<Ingredient> aitemstack = NonNullList.withSize(shape.width * shape.height, Ingredient.EMPTY);

		for(int l = 0; l < shape.width * shape.height; ++l)
		{
			char c0 = s.charAt(l);
			if(dictionary.containsKey(c0)) aitemstack.set(l, dictionary.get(c0));
		}

		event.add(new ShapedRecipe(getIdentifier(), group, shape.width, shape.height, aitemstack, result));
	}
}