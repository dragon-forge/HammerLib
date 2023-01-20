package org.zeith.api.level;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface ISpoofedRecipeManager
{
	Map<ResourceLocation, List<ResourceLocation>> getSpoofedRecipes();
}