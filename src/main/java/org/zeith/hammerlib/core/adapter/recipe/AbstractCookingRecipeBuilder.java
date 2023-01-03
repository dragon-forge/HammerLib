package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public abstract class AbstractCookingRecipeBuilder<R extends AbstractCookingRecipeBuilder<R>>
		extends SingleItemRecipeBuilder<R>
{
	protected int cookTime = 200;
	protected float xp = 0;
	protected CookingBookCategory category = CookingBookCategory.MISC;
	
	public AbstractCookingRecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public R cookTime(int time)
	{
		this.cookTime = time;
		return (R) this;
	}
	
	public R category(CookingBookCategory cat)
	{
		this.category = cat;
		return (R) this;
	}
	
	public R xp(float xp)
	{
		this.xp = xp;
		return (R) this;
	}
	
	protected abstract Recipe<?> generateRecipe();
	
	@Override
	public void register()
	{
		validate();
		
		var rec = generateRecipe();
		event.register(rec.getId(), rec);
	}
}