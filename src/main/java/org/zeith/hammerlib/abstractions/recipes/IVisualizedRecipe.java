package org.zeith.hammerlib.abstractions.recipes;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.abstractions.recipes.layout.IVisualizerBuilder;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface IVisualizedRecipe<T extends Recipe<?>>
{
	RecipeHolder<T> getRecipe();
	
	void setupLayout(IVisualizerBuilder builder);
	
	default void drawBackground(GuiGraphics gfx, double mouseX, double mouseY)
	{
	}
	
	default List<Component> getTooltipStrings(double mouseX, double mouseY)
	{
		return List.of();
	}
	
	default boolean handleInput(double mouseX, double mouseY, InputConstants.Key input)
	{
		return false;
	}
}