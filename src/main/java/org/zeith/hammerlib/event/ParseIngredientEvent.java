package org.zeith.hammerlib.event;

import lombok.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.Event;

/**
 * A simple event fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS} used to parse custom ingredients.
 * They may be of absolutely any type -- it's up to you how to parse it!
 */
@Getter
public class ParseIngredientEvent
		extends Event
{
	protected final Object component;
	
	@Setter
	protected Ingredient ingredient;
	
	public ParseIngredientEvent(Object component)
	{
		this.component = component;
	}
	
	public boolean hasIngredient()
	{
		return ingredient != null;
	}
}