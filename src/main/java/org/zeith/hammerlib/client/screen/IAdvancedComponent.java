package org.zeith.hammerlib.client.screen;

import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public interface IAdvancedComponent
{
	default List<Rect2i> getExtraAreas()
	{
		return List.of();
	}
	
	default Object getIngredientUnderMouse(double mouseX, double mouseY)
	{
		return null;
	}
}