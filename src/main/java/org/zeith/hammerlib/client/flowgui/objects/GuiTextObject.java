package org.zeith.hammerlib.client.flowgui.objects;

import lombok.Getter;
import lombok.With;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.client.flowgui.*;

public class GuiTextObject
		extends GuiObject
{
	protected @Getter Font font;
	protected @Getter FormattedCharSequence text;
	public int color;
	public boolean shadow;
	
	public GuiTextObject(String name, Font font, FormattedCharSequence text, int color, boolean shadow)
	{
		super(name);
		this.font = font;
		this.text = text;
		this.color = color;
		this.shadow = shadow;
		updateDimensions();
	}
	
	public GuiTextObject setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	public GuiTextObject setShadow(boolean shadow)
	{
		this.shadow = shadow;
		return this;
	}
	
	public GuiTextObject setFont(Font font)
	{
		this.font = font;
		updateDimensions();
		return this;
	}
	
	public GuiTextObject setText(Component text)
	{
		return setText(text.getVisualOrderText());
	}
	
	public GuiTextObject setText(FormattedCharSequence text)
	{
		this.text = text;
		updateDimensions();
		return this;
	}
	
	public void updateDimensions()
	{
		this.width = font.width(text);
		this.height = font.lineHeight;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		gfx.drawString(font, text, 0, 0, color, shadow);
	}
}