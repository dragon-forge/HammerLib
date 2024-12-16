package org.zeith.api.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.*;

public interface ISpoofedRecipeManager
{
	Optional<? extends RecipeHolder<?>> findFirstRecipeHL(Collection<ResourceKey<Recipe<?>>> ids);
	
	Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> getSpoofedRecipesHL();
}