package org.zeith.hammerlib.api.crafting;

import net.minecraft.resources.ResourceLocation;

public interface INameableRecipe
		extends IGeneralRecipe
{
	ResourceLocation getRecipeName();
}