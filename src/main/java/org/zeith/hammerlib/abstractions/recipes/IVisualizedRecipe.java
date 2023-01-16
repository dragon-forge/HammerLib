package org.zeith.hammerlib.abstractions.recipes;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.abstractions.recipes.layout.IVisualizerBuilder;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface IVisualizedRecipe<T extends Recipe<?>>
{
	T getRecipe();
	
	void setupLayout(IVisualizerBuilder builder);
	
	default void drawBackground(PoseStack poseStack, double mouseX, double mouseY)
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