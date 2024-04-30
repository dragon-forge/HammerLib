package org.zeith.api.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.*;

public interface ISpoofedRecipeManager
{
	Optional<? extends Recipe<?>> findFirstRecipeHL(Collection<ResourceLocation> ids);
	
	Map<ResourceLocation, List<ResourceLocation>> getSpoofedRecipesHL();
}