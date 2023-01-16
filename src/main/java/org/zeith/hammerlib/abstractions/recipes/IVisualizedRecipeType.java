package org.zeith.hammerlib.abstractions.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Consumer;

public interface IVisualizedRecipeType<T extends Recipe<?>>
		extends RecipeType<T>
{
	void initVisuals(Consumer<IRecipeVisualizer<T, ?>> viualizerConsumer);
}