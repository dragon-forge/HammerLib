package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipeBuilder
		extends RecipeBuilder<ShapedRecipeBuilder, Recipe<?>>
{
	protected final Map<Character, Ingredient> dictionary = new HashMap<>();
	protected RecipeShape shape;
	protected CraftingBookCategory category = CraftingBookCategory.MISC;
	
	public ShapedRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public ShapedRecipeBuilder category(CraftingBookCategory cat)
	{
		this.category = cat;
		return this;
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
	protected void validate()
	{
		super.validate();
		if(shape == null)
			throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined shape!");
		if(dictionary.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have any defined ingredients!");
	}
	
	@Override
	public void register()
	{
		validate();
		if(!event.enableRecipe(getIdentifier())) return;
		
		var id = getIdentifier();
		event.register(id, new ShapedRecipe(id, group, category, shape.width, shape.height, shape.createIngredientMap(dictionary), result));
	}
}