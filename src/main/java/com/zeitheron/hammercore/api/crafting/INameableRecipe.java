package com.zeitheron.hammercore.api.crafting;

import net.minecraft.util.ResourceLocation;

public interface INameableRecipe extends IGeneralRecipe
{
	ResourceLocation getRecipeName();
}