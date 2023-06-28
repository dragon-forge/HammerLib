package org.zeith.hammerlib.compat.jei;

import org.zeith.hammerlib.util.java.reflection.Accessor;

public class JEIRecipeTypes
{
	public static final Accessor CRAFTING = $("CRAFTING");
	public static final Accessor STONECUTTING = $("STONECUTTING");
	public static final Accessor SMELTING = $("SMELTING");
	public static final Accessor SMOKING = $("SMOKING");
	public static final Accessor BLASTING = $("BLASTING");
	public static final Accessor CAMPFIRE_COOKING = $("CAMPFIRE_COOKING");
	public static final Accessor FUELING = $("FUELING");
	public static final Accessor BREWING = $("BREWING");
	public static final Accessor ANVIL = $("ANVIL");
	public static final Accessor SMITHING = $("SMITHING");
	public static final Accessor COMPOSTING = $("COMPOSTING");
	public static final Accessor INFORMATION = $("INFORMATION");
	
	private static Accessor $(String v)
	{
		return new Accessor("mezz.jei.api.constants.RecipeTypes", v);
	}
}