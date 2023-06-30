package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;

public interface IRecipeRegistrationEvent<T>
		extends IResourceRegistrar<T>, IResourceLocationGenerator<Item>
{
	boolean enableRecipe(RecipeType<?> type, ResourceLocation recipeId);
}