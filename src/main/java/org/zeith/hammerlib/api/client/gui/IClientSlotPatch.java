package org.zeith.hammerlib.api.client.gui;

import org.zeith.hammerlib.client.flowgui.util.ISlotLink;
import org.zeith.hammerlib.util.math.Point;

/**
 * Applied to all {@link net.minecraft.world.inventory.Slot} on client side.
 */
public interface IClientSlotPatch
{
	Point getPrevPos();
	
	void setPrevPos(Point pos);
	
	void setPos(Point pos);
	
	ISlotLink getLinkedHover();
	
	void setLinkedHover(ISlotLink link);
}