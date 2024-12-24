package org.zeith.hammerlib.client.flowgui.objects;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.math.Point;

import java.util.function.*;

public class GuiEditBoxObject
		extends GuiObject
		implements TextBoxProperties<GuiEditBoxObject>
{
	public EditBox wrapped;
	public final Consumer<EditBox> initializer;
	
	@Builder(builderClassName = "EditBoxBuilder")
	public GuiEditBoxObject(String name, Consumer<EditBox> initializer)
	{
		super(name);
		this.initializer = initializer;
		this.wrapped = createWrapped();
	}
	
	protected EditBox createWrapped()
	{
		var box = new EditBox(Minecraft.getInstance().font, 100, 20, Component.empty());
		if(initializer != null) initializer.accept(box);
		return box;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		wrapped.setWidth((int) width);
		wrapped.setHeight((int) height);
		wrapped.render(gfx.gfx(), (int) pos.relX(), (int) pos.relY(), gfx.partialTime());
	}
	
	@Override
	protected boolean onMouseDragged(Point globalMousePos, MousePos pos, int button, MousePos dragPos)
	{
		return wrapped.mouseDragged(pos.relX(), pos.relY(), button, dragPos.relX(), dragPos.relY());
	}
	
	@Override
	protected boolean onCharTyped(char codePoint, int modifiers)
	{
		return wrapped.charTyped(codePoint, modifiers);
	}
	
	@Override
	protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers)
	{
		return this.wrapped.keyPressed(keyCode, scanCode, modifiers) || this.wrapped.canConsumeInput();
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
	
	public static EditBoxBuilder builder(String name)
	{
		return new EditBoxBuilder()
				.name(name);
	}
	
	@Override
	public GuiEditBoxObject initializer(Consumer<EditBox> initializer)
	{
		if(wrapped != null)
			initializer.accept(wrapped);
		return this;
	}
	
	public static class EditBoxBuilder
			implements TextBoxProperties<EditBoxBuilder>
	{
		private EditBoxBuilder name(String name)
		{
			this.name = name;
			return this;
		}
		
		@Override
		public EditBoxBuilder initializer(Consumer<EditBox> initializer)
		{
			if(this.initializer == null) this.initializer = initializer;
			else this.initializer = this.initializer.andThen(initializer);
			return this;
		}
	}
}