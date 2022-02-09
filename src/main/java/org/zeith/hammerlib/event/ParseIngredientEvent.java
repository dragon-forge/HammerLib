package org.zeith.hammerlib.event;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.GenericEvent;

/**
 * A simple event fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS} used to parse custom ingredients.
 * They may be of absolutely any type -- it's up to you how to parse it!
 */
public class ParseIngredientEvent<T>
		extends GenericEvent<T>
{
	final T component;
	Ingredient ingredient;

	public ParseIngredientEvent(T component)
	{
		super((Class<T>) component.getClass());
		this.component = component;
	}

	public T getComponent()
	{
		return component;
	}

	public void setIngredient(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}

	public Ingredient getIngredient()
	{
		return ingredient;
	}

	public boolean hasIngredient()
	{
		return ingredient != null;
	}
}