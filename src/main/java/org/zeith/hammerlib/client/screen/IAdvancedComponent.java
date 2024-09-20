package org.zeith.hammerlib.client.screen;

import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public interface IAdvancedComponent
{
	List<Rect2i> getExtraAreas();
	
	Object getIngredientUnderMouse(double mouseX, double mouseY);
}