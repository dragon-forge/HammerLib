package org.zeith.hammerlib.mixins.client;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;
import org.zeith.hammerlib.api.client.gui.IClientSlotPatch;
import org.zeith.hammerlib.client.flowgui.util.ISlotLink;
import org.zeith.hammerlib.util.math.Point;
import org.zeith.hammerlib.util.java.Cast;

@Mixin(Slot.class)
public class SlotMixin
		implements IClientSlotPatch
{
	@Mutable
	@Shadow
	@Final
	public int x;
	
	@Mutable
	@Shadow
	@Final
	public int y;
	
	@Unique
	private ISlotLink hl$link;
	
	@Unique
	private Point hl$prevPos;
	
	@Override
	public Point getPrevPos()
	{
		return hl$prevPos;
	}
	
	@Override
	public void setPrevPos(Point pos)
	{
		hl$prevPos = pos;
	}
	
	@Override
	public void setPos(Point pos)
	{
		x = (int) pos.x();
		y = (int) pos.y();
	}
	
	@Override
	public ISlotLink getLinkedHover()
	{
		if(hl$link != null && !hl$link.isValidFor(Cast.cast(this)))
		{
			if(hl$prevPos != null)
			{
				setPos(hl$prevPos);
				hl$prevPos = null;
			}
			hl$link = null;
		}
		return hl$link;
	}
	
	@Override
	public void setLinkedHover(ISlotLink link)
	{
		setPrevPos(new Point(x, y));
		hl$link = link;
	}
}