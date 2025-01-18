package org.zeith.hammerlib.core.adapter.recipe;

public record RecipeGroup(String mod, String item)
{
	public static final RecipeGroup NONE = new RecipeGroup("", "");
	
	@Override
	public String toString()
	{
		return mod.isEmpty() ? "" : mod + ":" + item;
	}
}