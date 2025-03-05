package org.zeith.hammerlib.client.flowgui.objects;

import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.math.Point;

public class GuiHoverObject
		extends GuiObject
{
	public boolean hover;
	
	public boolean alwaysRenderChildren;
	
	public GuiHoverObject(String name)
	{
		super(name);
	}
	
	public GuiHoverObject alwaysRenderChildren(boolean alwaysRenderChildren)
	{
		this.alwaysRenderChildren = alwaysRenderChildren;
		return this;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		this.hover = pos.isMouseWithin(this);
		super.render(gfx, pos);
	}
	
	@Override
	protected void renderChildren(Graphics g, Point globalMousePos)
	{
		if(!alwaysRenderChildren && !hover) return;
		super.renderChildren(g, globalMousePos);
		if(hover)
		{
			var pose = g.pose();
			pose.pushPose();
			pose.scale(width, height, width);
			g.fill(0, 0, 1, 1, 0x88FFFFFF);
			pose.popPose();
		}
	}
}