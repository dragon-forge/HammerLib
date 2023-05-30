package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface IRecipeRegistrationEvent<T>
		extends IResourceRegistrar<T>, IResourceLocationGenerator<Item>
{
	boolean enableRecipe(ResourceLocation recipeId);
}