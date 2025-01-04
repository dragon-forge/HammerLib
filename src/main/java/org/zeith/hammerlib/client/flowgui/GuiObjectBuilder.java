package org.zeith.hammerlib.client.flowgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.Slot;
import org.zeith.hammerlib.client.flowgui.objects.*;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

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
	
	public GuiImageObject fullImage(
			ResourceLocation tex,
			float width, float height
	)
	{
		return image(tex, 0, 0, width, height, width, height);
	}
	
	public GuiImageObject image(
			ResourceLocation tex,
			float uOffset, float vOffset,
			float width, float height,
			float txWidth, float txHeight)
	{
		return new GuiImageObject(name, Cast.constant(tex), uOffset, vOffset, width, height, txWidth, txHeight);
	}
	
	public GuiImageObject image(
			ResourceLocation tex,
			float uOffset, float vOffset,
			float width, float height)
	{
		return image(tex, uOffset, vOffset, width, height, 256, 256);
	}
	
	
	
	public GuiImageObject fullImage(
			Supplier<ResourceLocation> tex,
			float width, float height
	)
	{
		return image(tex, 0, 0, width, height, width, height);
	}
	
	public GuiImageObject image(
			Supplier<ResourceLocation> tex,
			float uOffset, float vOffset,
			float width, float height,
			float txWidth, float txHeight)
	{
		return new GuiImageObject(name, tex, uOffset, vOffset, width, height, txWidth, txHeight);
	}
	
	public GuiImageObject image(
			Supplier<ResourceLocation> tex,
			float uOffset, float vOffset,
			float width, float height)
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