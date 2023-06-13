package org.zeith.hammerlib.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.abstractions.recipes.IRecipeVisualizer;
import org.zeith.hammerlib.abstractions.recipes.IVisualizedRecipe;
import org.zeith.hammerlib.compat.jei.absimpl.VisualizerBuilderJEI;

import java.util.List;

public class VisualizedRecipeCategory<T extends IVisualizedRecipe<?>>
		implements IRecipeCategory<T>
{
	public final RecipeType<T> type;
	public final IRecipeVisualizer.VisualizedRecipeGroup group;
	
	private final IDrawable bg = new IDrawable()
	{
		@Override
		public int getWidth()
		{
			return group.width();
		}
		
		@Override
		public int getHeight()
		{
			return group.height();
		}
		
		@Override
		public void draw(GuiGraphics poseStack, int xOffset, int yOffset)
		{
		}
	};
	
	private final IDrawable icon = new IDrawable()
	{
		@Override
		public int getWidth()
		{
			return 16;
		}
		
		@Override
		public int getHeight()
		{
			return 16;
		}
		
		@Override
		public void draw(GuiGraphics poseStack, int xOffset, int yOffset)
		{
			var ico = group.icon();
			if(ico != null) ico.render(poseStack, xOffset, yOffset, 16, 16);
		}
	};
	
	public VisualizedRecipeCategory(RecipeType<T> type, IRecipeVisualizer.VisualizedRecipeGroup group)
	{
		this.type = type;
		this.group = group;
	}
	
	@Override
	public RecipeType<T> getRecipeType()
	{
		return type;
	}
	
	@Override
	public Component getTitle()
	{
		return group.title();
	}
	
	@Override
	public IDrawable getBackground()
	{
		return bg;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focus)
	{
		recipe.setupLayout(new VisualizerBuilderJEI(builder));
	}
	
	@Override
	public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gfx, double mouseX, double mouseY)
	{
		recipe.drawBackground(gfx, mouseX, mouseY);
	}
	
	@Override
	public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		return recipe.getTooltipStrings(mouseX, mouseY);
	}
	
	@Override
	public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input)
	{
		return recipe.handleInput(mouseX, mouseY, input);
	}
	
	@Override
	public @Nullable ResourceLocation getRegistryName(T recipe)
	{
		return recipe.getRecipe().getId();
	}
}
