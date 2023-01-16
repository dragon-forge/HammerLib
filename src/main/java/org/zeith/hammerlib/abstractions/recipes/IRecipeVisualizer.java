package org.zeith.hammerlib.abstractions.recipes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.client.utils.UV;

import java.util.function.Function;

public interface IRecipeVisualizer<T extends Recipe<?>, VIS extends IVisualizedRecipe<T>>
{
	VisualizedRecipeGroup getGroup();
	
	Class<VIS> getVisualizedType();
	
	VIS visualize(T recipe);
	
	static <T extends Recipe<?>, VIS extends IVisualizedRecipe<T>> IRecipeVisualizer<T, VIS> simple(Class<VIS> baseVisType, VisualizedRecipeGroup group, Function<T, VIS> fun)
	{
		return new IRecipeVisualizer<>()
		{
			@Override
			public VisualizedRecipeGroup getGroup()
			{
				return group;
			}
			
			@Override
			public Class<VIS> getVisualizedType()
			{
				return baseVisType;
			}
			
			@Override
			public VIS visualize(T recipe)
			{
				return fun.apply(recipe);
			}
		};
	}
	
	record VisualizedRecipeGroup(Component title, int width, int height, UV icon)
	{
	}
}