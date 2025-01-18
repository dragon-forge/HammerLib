package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

public abstract class RecipeBuilder<R extends RecipeBuilder<R>>
		extends AbstractRecipeBuilder<R>
{
	protected ItemStack result = ItemStack.EMPTY;
	
	public RecipeBuilder(IRecipeRegistrationEvent<Recipe<?>> event)
	{
		super(event);
	}
	
	public R result(ItemStack stack)
	{
		this.result = stack;
		return (R) this;
	}
	
	public R result(ItemLike provider)
	{
		this.result = new ItemStack(provider);
		return (R) this;
	}
	
	public R result(ItemLike provider, int count)
	{
		this.result = new ItemStack(provider, count);
		return (R) this;
	}
	
	@Override
	protected ResourceLocation getResultIdentifier()
	{
		return BuiltInRegistries.ITEM.getKey(result.getItem());
	}
	
	@Override
	protected void validate()
	{
		if(result.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined result!");
	}
}