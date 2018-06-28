package com.zeitheron.hammercore.internal.items;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Implementation for {@link Item}s that use custom tooltip. Allows to inject
 * variables. Can be used to make dynamic tooltips with custom vars. They are
 * replaced with $VARNAME There is now calculation feature. Use it like this:
 * parse[PI * 2 + (-2) ^ 2]
 */
public interface ITooltipInjector
{
	/**
	 * Injects variables to vars map. Use vars.put("test", "This is a test
	 * variable!"); Will replace "$test" with "This is a test variable!"
	 */
	public void injectVariables(ItemStack stack, Map<String, String> vars);
}