package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.api.recipes.IngredientWithCount;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.mcf.itf.IRecipeRegistrationEvent;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public abstract class RecipeBuilder<R extends RecipeBuilder<R, RT>, RT>
{
	protected final IRecipeRegistrationEvent<RT> event;
	protected ResourceLocation identifier;
	protected String group = "";
	protected ItemStack result = ItemStack.EMPTY;
	
	public RecipeBuilder(IRecipeRegistrationEvent<RT> event)
	{
		this.event = event;
	}
	
	/**
	 * Optional
	 */
	public R id(ResourceLocation identifier)
	{
		this.identifier = identifier;
		return (R) this;
	}
	
	public R group(String group)
	{
		this.group = group;
		return (R) this;
	}
	
	public R group(RecipeGroup group)
	{
		this.group = group.toString();
		return (R) this;
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
	
	protected ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return this.identifier = event.nextId(result.getItem());
	}
	
	protected void validate()
	{
		if(result.isEmpty())
			throw new IllegalStateException(getClass().getSimpleName() + " does not have a defined result!");
	}
	
	public abstract void register();
	
	public void registerIf(BooleanSupplier condition)
	{
		if(condition.getAsBoolean())
			register();
	}
	
	public void registerIf(Predicate<ResourceLocation> condition)
	{
		if(condition.test(identifier))
			register();
	}
	
	protected Ingredient parseIngredient(Object obj)
	{
		return RecipeHelper.fromComponent(obj);
	}
	
	protected IngredientWithCount parseIngredient(Object obj, int count)
	{
		return new IngredientWithCount(RecipeHelper.fromComponent(obj), count);
	}
}