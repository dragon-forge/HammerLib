package org.zeith.hammerlib.core.adapter.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

import java.util.function.*;

public abstract class RecipeBuilder<R extends RecipeBuilder<R>>
{
	protected final RegisterRecipesEvent event;
	protected ResourceLocation identifier;
	protected String group = "";
	protected ItemStack result = ItemStack.EMPTY;

	public RecipeBuilder(RegisterRecipesEvent event)
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

	public R result(IItemProvider provider)
	{
		this.result = new ItemStack(provider);
		return (R) this;
	}

	public R result(IItemProvider provider, int count)
	{
		this.result = new ItemStack(provider, count);
		return (R) this;
	}
	
	public ResourceLocation getIdentifier()
	{
		if(this.identifier != null) return this.identifier;
		return event.nextId(result.getItem());
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
}