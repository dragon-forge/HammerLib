package com.zeitheron.hammercore.bookAPI.fancy;

/**
 * A custom rendering class for a {@link ManualPage}
 */
public interface IManualPageRender<T extends ManualPage>
{
	/**
	 * Renders a {@link ManualPage}.
	 * 
	 * @param recipe
	 *            The page to be rendered.
	 * @param side
	 *            The side rendered, either 0 or 1 - 0 = left, 1 = right
	 * @param x
	 *            The start X coord to render
	 * @param y
	 *            The start Y coord to render
	 * @param mx
	 *            The mouse current x position
	 * @param my
	 *            The mouse current y position
	 */
	void render(T recipe, int side, int x, int y, int mx, int my, GuiManualRecipe gui);
}