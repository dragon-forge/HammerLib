package com.pengu.hammercore.bookAPI.fancy;

public interface iManualPageRender<T extends ManualPage>
{
	void render(T recipe, int side, int x, int y, int mx, int my, GuiManualRecipe gui);
}