package org.zeith.hammerlib.client.flowgui.objects.base;

import net.minecraft.client.gui.components.AbstractWidget;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.flowgui.util.ScrollData;
import org.zeith.hammerlib.util.math.Point;

import java.util.function.Supplier;

public class GuiWidgetWrapperObject<W extends AbstractWidget>
		extends GuiObject
{
	public W wrapped;
	
	public GuiWidgetWrapperObject(String name)
	{
		super(name);
	}
	
	public GuiWidgetWrapperObject(String name, W wrapped)
	{
		super(name);
		this.wrapped = wrapped;
	}
	
	public GuiWidgetWrapperObject(String name, Supplier<W> wrapped)
	{
		this(name, wrapped.get());
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		wrapped.setX(0);
		wrapped.setY(0);
		wrapped.setWidth((int) width);
		wrapped.setHeight((int) height);
		wrapped.render(gfx.gfx(), (int) pos.relX(), (int) pos.relY(), gfx.partialTime());
	}
	
	@Override
	protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos)
	{
		return wrapped.isFocused() && wrapped.mouseDragged(pos.relX(), pos.relY(), button, dragPos.relX(), dragPos.relY());
	}
	
	@Override
	protected boolean onMouseReleased(Point globalMousePos, MousePos pos, int button)
	{
		return wrapped.mouseReleased(pos.relX(), pos.relY(), button);
	}
	
	@Override
	protected boolean onMouseScrolled(Point globalMousePos, MousePos pos, ScrollData delta)
	{
		return pos.isMouseWithin(this) && wrapped.mouseScrolled(pos.relX(), pos.relY(), delta.scrollY(), delta.scrollY());
	}
	
	@Override
	protected void onMouseMoved(Point globalMousePos, MousePos pos)
	{
		if(pos.isMouseWithin(this))
			wrapped.mouseMoved(pos.relX(), pos.relY());
	}
	
	@Override
	protected boolean onCharTyped(char codePoint, int modifiers)
	{
		return wrapped.charTyped(codePoint, modifiers);
	}
	
	@Override
	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers)
	{
		return wrapped.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	protected boolean onKeyReleased(int keyCode, int scanCode, int modifiers)
	{
		return wrapped.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	protected boolean onMouseClicked(Point globalMousePos, MousePos pos, int button, boolean fake)
	{
		boolean mouseInside = pos.isMouseWithin(width, height);
		if(button == 0 && !fake && wrapped.isFocused() != mouseInside)
		{
			wrapped.setFocused(mouseInside);
			return true;
		}
		
		return fake || wrapped.mouseClicked(pos.relX(), pos.relY(), button);
	}
}