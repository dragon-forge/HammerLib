package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.*;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public abstract class SingleItemRecipeBuilder<R extends SingleItemRecipeBuilder<R>>
		extends RecipeBuilderMC<R>
{
	protected Ingredient input = Ingredient.EMPTY;
	
	public SingleItemRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public R input(Object in)
	{
		this.input = RecipeHelper.fromComponent(in);
		return (R) this;
	}
	
	@Override
	protected void validate()
	{
		super.validate();
		if(input == Ingredient.EMPTY)
			throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined input!");
	}
}