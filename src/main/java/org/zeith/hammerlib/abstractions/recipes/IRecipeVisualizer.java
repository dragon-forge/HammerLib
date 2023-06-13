package org.zeith.hammerlib.abstractions.recipes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.zeith.hammerlib.client.render.IGuiDrawable;

import java.util.*;
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
	
	/**
	 * Use this to maintain backwards compatibility!
	 */
	static VisualizedRecipeGroupBuilder groupBuilder()
	{
		return new VisualizedRecipeGroupBuilder();
	}
	
	record VisualizedRecipeGroup(
			Component title,
			int width, int height,
			IGuiDrawable icon,
			List<ItemStack> catalyst
	)
	{
	}
	
	class VisualizedRecipeGroupBuilder
	{
		protected Component title = Component.literal("");
		protected int width = 116, height = 54;
		protected IGuiDrawable icon = IGuiDrawable.EMPTY;
		protected List<ItemStack> catalysts = new ArrayList<>();
		
		public VisualizedRecipeGroupBuilder title(Component title)
		{
			this.title = title;
			return this;
		}
		
		public VisualizedRecipeGroupBuilder size(int width, int height)
		{
			this.width = width;
			this.height = height;
			return this;
		}
		
		public VisualizedRecipeGroupBuilder icon(IGuiDrawable icon)
		{
			this.icon = icon;
			return this;
		}
		
		public VisualizedRecipeGroupBuilder catalyst(ItemStack... items)
		{
			catalysts.addAll(List.of(items));
			return this;
		}
		
		public VisualizedRecipeGroupBuilder catalyst(Collection<ItemStack> items)
		{
			catalysts.addAll(List.copyOf(items));
			return this;
		}
		
		public VisualizedRecipeGroup build()
		{
			return new VisualizedRecipeGroup(
					title,
					width, height,
					icon,
					catalysts
			);
		}
	}
}