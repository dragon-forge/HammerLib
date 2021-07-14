package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.crafting.IRecipe;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

public abstract class AbstractCookingRecipeBuilder<R extends AbstractCookingRecipeBuilder<R>>
		extends SingleItemRecipeBuilder<R>
{
	protected int cookTime = 200;
	protected float xp = 0;

	public AbstractCookingRecipeBuilder(RegisterRecipesEvent event)
	{
		super(event);
	}

	public R cookTime(int time)
	{
		this.cookTime = time;
		return (R) this;
	}

	public R xp(float xp)
	{
		this.xp = xp;
		return (R) this;
	}

	protected abstract IRecipe<?> generateRecipe();

	@Override
	public void register()
	{
		validate();
		event.add(generateRecipe());
	}
}