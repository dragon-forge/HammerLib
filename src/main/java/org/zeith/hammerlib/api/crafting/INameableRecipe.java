package org.zeith.hammerlib.api.crafting;

import net.minecraft.util.ResourceLocation;

public interface INameableRecipe
		extends IGeneralRecipe
{
	ResourceLocation getRecipeName();
}