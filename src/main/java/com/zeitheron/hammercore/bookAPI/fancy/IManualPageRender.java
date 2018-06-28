package com.zeitheron.hammercore.bookAPI.fancy;

public interface IManualPageRender<T extends ManualPage>
{
	void render(T recipe, int side, int x, int y, int mx, int my, GuiManualRecipe gui);
}