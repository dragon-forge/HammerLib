package org.zeith.hammerlib.client.flowgui;

import org.zeith.hammerlib.util.math.Point;

/**
 * Contains global and relative mouse pos.
 * <p>
 * Relative X and Y always adapt to {@link GuiObject}'s transforms (pos, rotation, scale)
 */
public record MousePos(Point globalPos, float relX, float relY)
{
	public boolean isMouseWithin(GuiObject object)
	{
		return isMouseWithin(object.width, object.height);
	}
	
	public boolean isMouseWithin(float width, float height)
	{
		return relX >= 0 && relY >= 0 && relX < width && relY < height;
	}
}