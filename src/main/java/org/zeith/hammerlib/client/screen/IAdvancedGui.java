package org.zeith.hammerlib.client.screen;

import net.minecraft.client.renderer.Rect2i;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

/**
 * An abstract interface to be applied
 */
@OnlyIn(Dist.CLIENT)
public interface IAdvancedGui
		extends IAdvancedComponent
{
	@Override
	default List<Rect2i> getExtraAreas()
	{
		return List.of();
	}
	
	@Override
	default Object getIngredientUnderMouse(double mouseX, double mouseY)
	{
		return null;
	}
}