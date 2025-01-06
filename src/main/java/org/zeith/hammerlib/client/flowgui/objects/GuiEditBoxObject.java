package org.zeith.hammerlib.client.flowgui.objects;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.zeith.hammerlib.client.flowgui.objects.base.GuiWidgetWrapperObject;

import java.util.function.Consumer;

public class GuiEditBoxObject
		extends GuiWidgetWrapperObject<EditBox>
		implements TextBoxProperties<GuiEditBoxObject>
{
	@Builder(builderClassName = "EditBoxBuilder")
	public GuiEditBoxObject(String name, Consumer<EditBox> initializer)
	{
		super(name, () ->
				{
					var box = new EditBox(Minecraft.getInstance().font, 100, 20, Component.empty());
					if(initializer != null) initializer.accept(box);
					return box;
				}
		);
	}
	
	public String getText()
	{
		return wrapped.getValue();
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