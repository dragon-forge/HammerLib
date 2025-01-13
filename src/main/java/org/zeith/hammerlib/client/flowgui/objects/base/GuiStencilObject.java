package org.zeith.hammerlib.client.flowgui.objects.base;

import org.zeith.hammerlib.client.flowgui.Graphics;
import org.zeith.hammerlib.client.flowgui.GuiObject;
import org.zeith.hammerlib.client.utils.GLStencil;
import org.zeith.hammerlib.util.math.Point;

import java.util.ArrayList;
import java.util.List;

public class GuiStencilObject
		extends GuiObject
{
	protected final List<GuiObject> stencilChildren = new ArrayList<>();
	
	public GuiStencilObject(String name)
	{
		super(name);
	}
	
	public GuiStencilObject addStencil(GuiObject object)
	{
		stencilChildren.add(object);
		return this;
	}
	
	@Override
	protected void renderChildren(Graphics g, Point globalMousePos)
	{
		try(var s = GLStencil.of())
		{
			s.populateStencil(g.gfx(), g2 ->
					{
						var graphics = g.withGfx(g2);
						for(GuiObject child : stencilChildren)
							child.renderObject(graphics, globalMousePos);
					}
			);
			
			s.renderWithStencil(() -> super.renderChildren(g, globalMousePos));
		}
	}
}