package org.zeith.hammerlib.client.flowgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.Slot;
import org.zeith.hammerlib.client.flowgui.objects.*;

public class GuiObjectBuilder
{
	private final String name;
	
	public GuiObjectBuilder(String name)
	{
		this.name = name;
	}
	
	public static GuiObjectBuilder named(String name)
	{
		return new GuiObjectBuilder(name);
	}
	
	public GuiObject empty()
	{
		return new GuiObject(name);
	}
	
	public GuiSlotLinkObject slot(Slot slot)
	{
		return new GuiSlotLinkObject(name).bindToSlot(slot);
	}
	
	public GuiImageObject image(
			ResourceLocation tex,
			float uOffset, float vOffset,
			int width, int height,
			int txWidth, int txHeight)
	{
		return new GuiImageObject(name, tex, uOffset, vOffset, width, height, txWidth, txHeight);
	}
	
	public GuiImageObject image(
			ResourceLocation tex,
			float uOffset, float vOffset,
			int width, int height)
	{
		return image(tex, uOffset, vOffset, width, height, 256, 256);
	}
	
	public GuiTextObject text(Font font, FormattedCharSequence text, int color, boolean shadow)
	{
		return new GuiTextObject(name, font, text, color, shadow);
	}
	
	public GuiTextObject text(Component text, int color, boolean shadow)
	{
		return text(Minecraft.getInstance().font, text.getVisualOrderText(), color, shadow);
	}
	
	public GuiTextObject text(Component text)
	{
		return text(text, 0xFFFFFFFF, true);
	}
	
	public GuiButtonObject.ButtonBuilder button()
	{
		return GuiButtonObject.builder(name);
	}
	
	public GuiSpriteButtonObject.SpriteButtonBuilder spriteButton()
	{
		return GuiSpriteButtonObject.of(name);
	}
	
	public GuiEditBoxObject.EditBoxBuilder editBox()
	{
		return GuiEditBoxObject.builder(name);
	}
}