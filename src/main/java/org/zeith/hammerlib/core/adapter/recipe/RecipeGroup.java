package org.zeith.hammerlib.core.adapter.recipe;

public class RecipeGroup
{
	public static final RecipeGroup NONE = new RecipeGroup("", "");

	public final String mod;
	public final String item;

	public RecipeGroup(String mod, String item)
	{
		this.mod = mod;
		this.item = item;
	}

	@Override
	public String toString()
	{
		return mod.isEmpty() ? "" : mod + ":" + item;
	}
}