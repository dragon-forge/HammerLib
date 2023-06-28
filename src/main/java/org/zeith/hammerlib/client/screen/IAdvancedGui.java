package org.zeith.hammerlib.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.annotation.*;
import java.util.List;

/**
 * An abstract interface to be applied
 */
@SuppressWarnings("rawtypes")
@OnlyIn(Dist.CLIENT)
public interface IAdvancedGui<G extends AbstractContainerScreen & IAdvancedGui<G>>
{
	default List<Rect2i> getExtraAreas()
	{
		return List.of();
	}
	
	default Object getIngredientUnderMouse(double mouseX, double mouseY)
	{
		return null;
	}
	
	/**
	 * Annotating your {@link AbstractContainerScreen} class that implements {@link IAdvancedGui} will result in it getting wrapped for use with JEI.
	 * This allows you to tell JEI that your GUI has extra areas that should not be obstructed by its panels, or add a {@link net.minecraftforge.fluids.FluidStack} ingredient under fluid rendering stuff if you need to.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface ApplyToJEI
	{
	}
}