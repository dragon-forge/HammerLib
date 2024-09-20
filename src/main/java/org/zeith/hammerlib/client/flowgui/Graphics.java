package org.zeith.hammerlib.client.flowgui;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

@Builder
public record Graphics(GuiGraphics gfx, Minecraft game, float partialTime, boolean debugBounds)
{
	public void setColor(float r, float g, float b, float a)
	{
		gfx.setColor(r, g, b, a);
	}
	
	public void fill(int minX, int minY, int maxX, int maxY, int color)
	{
		gfx.fill(minX, minY, maxX, maxY, color);
	}
	
	public void renderItemDecorations(Font font, ItemStack stack, int x, int y)
	{
		gfx.renderItemDecorations(font, stack, x, y);
	}
	
	public void renderItemDecorations(Font font, ItemStack stack, int x, int y, String text)
	{
		gfx.renderItemDecorations(font, stack, x, y, text);
	}
	
	public void fillGradient(int minX, int minY, int maxX, int maxY, int colorFrom, int colorTo)
	{
		gfx.fillGradient(minX, minY, maxX, maxY, colorFrom, colorTo);
	}
	
	public void enableScissor(int minX, int minY, int maxX, int maxY)
	{
		gfx.enableScissor(minX, minY, maxX, maxY);
	}
	
	public void disableScissor()
	{
		gfx.disableScissor();
	}
	
	public int drawString(Font font, Component arg1, int arg2, int arg3, int arg4, boolean arg5)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4, arg5);
	}
	
	public int drawString(Font font, FormattedCharSequence arg1, int arg2, int arg3, int arg4)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4);
	}
	
	public int drawString(Font font, String arg1, float arg2, float arg3, int arg4, boolean arg5)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4, arg5);
	}
	
	public int drawString(Font font, String arg1, int arg2, int arg3, int arg4, boolean arg5)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4, arg5);
	}
	
	public int drawString(Font font, String arg1, int arg2, int arg3, int arg4)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4);
	}
	
	public int drawString(Font font, Component arg1, int arg2, int arg3, int arg4)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4);
	}
	
	public int drawString(Font font, FormattedCharSequence arg1, float arg2, float arg3, int arg4, boolean arg5)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4, arg5);
	}
	
	public int drawString(Font font, FormattedCharSequence arg1, int arg2, int arg3, int arg4, boolean arg5)
	{
		return gfx.drawString(font, arg1, arg2, arg3, arg4, arg5);
	}
	
	public void blit(ResourceLocation texture, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int txWidth, int txHeight)
	{
		gfx.blit(texture, x, y, blitOffset, uOffset, vOffset, uWidth, vHeight, txWidth, txHeight);
	}
	
	public void blit(ResourceLocation texture, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		gfx.blit(texture, x, y, uOffset, vOffset, uWidth, vHeight);
	}
	
	public void blit(int arg0, int arg1, int arg2, int arg3, int arg4, TextureAtlasSprite sprite)
	{
		gfx.blit(arg0, arg1, arg2, arg3, arg4, sprite);
	}
	
	public void blit(int arg0, int arg1, int arg2, int arg3, int arg4, TextureAtlasSprite arg5, float arg6, float arg7, float arg8, float arg9)
	{
		gfx.blit(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	public void blit(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, float arg5, float arg6, int arg7, int arg8, int arg9, int arg10)
	{
		gfx.blit(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	public void blit(ResourceLocation tex, int x, int y, float uOffset, float vOffset, int width, int height, int txWidth, int txHeight)
	{
		gfx.blit(tex, x, y, uOffset, vOffset, width, height, txWidth, txHeight);
	}
	
	public void blitNineSliced(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9)
	{
		gfx.blitNineSliced(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	public void blitNineSliced(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10)
	{
		gfx.blitNineSliced(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	public void blitNineSliced(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12)
	{
		gfx.blitNineSliced(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	public void blitRepeating(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10)
	{
		gfx.blitRepeating(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	public void blitRepeating(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8)
	{
		gfx.blitRepeating(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	public void renderItem(ItemStack arg0, int arg1, int arg2)
	{
		gfx.renderItem(arg0, arg1, arg2);
	}
	
	public void renderItem(ItemStack arg0, int arg1, int arg2, int arg3, int arg4)
	{
		gfx.renderItem(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void renderItem(ItemStack arg0, int arg1, int arg2, int arg3)
	{
		gfx.renderItem(arg0, arg1, arg2, arg3);
	}
	
	public void renderItem(LivingEntity arg0, ItemStack arg1, int arg2, int arg3, int arg4)
	{
		gfx.renderItem(arg0, arg1, arg2, arg3, arg4);
	}
	
	public int guiWidth()
	{
		return gfx.guiWidth();
	}
	
	public int guiHeight()
	{
		return gfx.guiHeight();
	}
	
	public void drawManaged(Runnable arg0)
	{
		gfx.drawManaged(arg0);
	}
	
	public void renderTooltip(Font arg0, List<Component> arg1, Optional<TooltipComponent> arg2, ItemStack arg3, int arg4, int arg5)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	
	public void renderTooltip(Font arg0, List<? extends FormattedCharSequence> arg1, int arg2, int arg3)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3);
	}
	
	public void renderTooltip(Font arg0, List<FormattedCharSequence> arg1, ClientTooltipPositioner arg2, int arg3, int arg4)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void renderTooltip(Font arg0, ItemStack arg1, int arg2, int arg3)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3);
	}
	
	public void renderTooltip(Font arg0, Component arg1, int arg2, int arg3)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3);
	}
	
	public void renderTooltip(Font arg0, List<Component> arg1, Optional<TooltipComponent> arg2, int arg3, int arg4)
	{
		gfx.renderTooltip(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void drawCenteredString(Font arg0, Component arg1, int arg2, int arg3, int arg4)
	{
		gfx.drawCenteredString(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void drawCenteredString(Font arg0, String arg1, int arg2, int arg3, int arg4)
	{
		gfx.drawCenteredString(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void drawCenteredString(Font arg0, FormattedCharSequence arg1, int arg2, int arg3, int arg4)
	{
		gfx.drawCenteredString(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void drawWordWrap(Font arg0, net.minecraft.network.chat.FormattedText arg1, int arg2, int arg3, int arg4, int arg5)
	{
		gfx.drawWordWrap(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	
	public void renderOutline(int arg0, int arg1, int arg2, int arg3, int arg4)
	{
		gfx.renderOutline(arg0, arg1, arg2, arg3, arg4);
	}
	
	public void renderFakeItem(ItemStack arg0, int arg1, int arg2)
	{
		gfx.renderFakeItem(arg0, arg1, arg2);
	}
	
	public int getColorFromFormattingCharacter(char arg0, boolean arg1)
	{
		return gfx.getColorFromFormattingCharacter(arg0, arg1);
	}
	
	public void blitNineSlicedSized(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12)
	{
		gfx.blitNineSlicedSized(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	public void blitNineSlicedSized(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int arg13, int arg14)
	{
		gfx.blitNineSlicedSized(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	public void blitNineSlicedSized(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11)
	{
		gfx.blitNineSlicedSized(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	public void blitWithBorder(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9)
	{
		gfx.blitWithBorder(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	public void blitWithBorder(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12)
	{
		gfx.blitWithBorder(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	public void blitInscribed(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6)
	{
		gfx.blitInscribed(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	public void blitInscribed(ResourceLocation arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, boolean arg7, boolean arg8)
	{
		gfx.blitInscribed(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
}