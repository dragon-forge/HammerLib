package org.zeith.hammerlib.client.flowgui;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
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
	
	public int drawString(Font font, Component text, int x, int y, int color, boolean shadow)
	{
		return gfx.drawString(font, text, x, y, color, shadow);
	}
	
	public int drawString(Font font, FormattedCharSequence text, int x, int y, int color)
	{
		return gfx.drawString(font, text, x, y, color);
	}
	
	public int drawString(Font font, String text, float x, float y, int color, boolean shadow)
	{
		return gfx.drawString(font, text, x, y, color, shadow);
	}
	
	public int drawString(Font font, String text, int x, int y, int color, boolean shadow)
	{
		return gfx.drawString(font, text, x, y, color, shadow);
	}
	
	public int drawString(Font font, String text, int x, int y, int color)
	{
		return gfx.drawString(font, text, x, y, color);
	}
	
	public int drawString(Font font, Component text, int x, int y, int color)
	{
		return gfx.drawString(font, text, x, y, color);
	}
	
	public int drawString(Font font, FormattedCharSequence text, float x, float y, int color, boolean shadow)
	{
		return gfx.drawString(font, text, x, y, color, shadow);
	}
	
	public int drawString(Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow)
	{
		return gfx.drawString(font, text, x, y, color, shadow);
	}
	
	public void blit(ResourceLocation tex, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int txWidth, int txHeight)
	{
		gfx.blit(tex, x, y, blitOffset, uOffset, vOffset, uWidth, vHeight, txWidth, txHeight);
	}
	
	public void blit(ResourceLocation tex, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		gfx.blit(tex, x, y, uOffset, vOffset, uWidth, vHeight);
	}
	
	public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite)
	{
		gfx.blit(x, y, z, width, height, sprite);
	}
	
	public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite, float red, float green, float blue, float alpha)
	{
		gfx.blit(x, y, z, width, height, sprite, red, green, blue, alpha);
	}
	
	public void blit(ResourceLocation tex, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int texWidth, int texHeight)
	{
		gfx.blit(tex, x, y, width, height, uOffset, vOffset, uWidth, vHeight, texWidth, texHeight);
	}
	
	public void blit(ResourceLocation tex, int x, int y, float uOffset, float vOffset, int width, int height, int txWidth, int txHeight)
	{
		gfx.blit(tex, x, y, uOffset, vOffset, width, height, txWidth, txHeight);
	}
	
	public void blitNineSliced(ResourceLocation tex, int x, int y, int width, int height, int sliceSize, int uOffset, int vOffset, int texWidth, int texHeight)
	{
		gfx.blitNineSliced(tex, x, y, width, height, sliceSize, uOffset, vOffset, texWidth, texHeight);
	}
	
	public void blitNineSliced(ResourceLocation tex, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int texX, int texY)
	{
		gfx.blitNineSliced(tex, x, y, width, height, sliceWidth, sliceHeight, uWidth, vHeight, texX, texY);
	}
	
	public void blitNineSliced(ResourceLocation tex, int x, int y, int width, int height, int leftSliceWidth, int topSliceHeight, int rightSliceWidth, int bottomSliceHeight, int uWidth, int vHeight, int texX, int texY)
	{
		gfx.blitNineSliced(tex, x, y, width, height, leftSliceWidth, topSliceHeight, rightSliceWidth, bottomSliceHeight, uWidth, vHeight, texX, texY);
	}
	
	public void blitRepeating(ResourceLocation tex, int x, int y, int width, int height, int uOffset, int vOffset, int sourceWidth, int sourceHeight, int texWidth, int texHeight)
	{
		gfx.blitRepeating(tex, x, y, width, height, uOffset, vOffset, sourceWidth, sourceHeight, texWidth, texHeight);
	}
	
	public void blitRepeating(ResourceLocation tex, int x, int y, int width, int height, int uOffset, int vOffset, int sourceWidth, int sourceHeight)
	{
		gfx.blitRepeating(tex, x, y, width, height, uOffset, vOffset, sourceWidth, sourceHeight);
	}
	
	public void renderItem(ItemStack stack, int x, int y)
	{
		gfx.renderItem(stack, x, y);
	}
	
	public void renderItem(ItemStack stack, int x, int y, int seed, int z)
	{
		gfx.renderItem(stack, x, y, seed, z);
	}
	
	public void renderItem(ItemStack stack, int x, int y, int seed)
	{
		gfx.renderItem(stack, x, y, seed);
	}
	
	public void renderItem(LivingEntity entity, ItemStack stack, int x, int y, int seed)
	{
		gfx.renderItem(entity, stack, x, y, seed);
	}
	
	public int guiWidth()
	{
		return gfx.guiWidth();
	}
	
	public int guiHeight()
	{
		return gfx.guiHeight();
	}
	
	public void drawManaged(Runnable command)
	{
		gfx.drawManaged(command);
	}
	
	public void renderTooltip(Font font, List<Component> tooltip, Optional<TooltipComponent> tooltipComponent, ItemStack stack, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, tooltip, tooltipComponent, stack, mouseX, mouseY);
	}
	
	public void renderTooltip(Font font, List<? extends FormattedCharSequence> tooltip, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, tooltip, mouseX, mouseY);
	}
	
	public void renderTooltip(Font font, List<FormattedCharSequence> tooltip, ClientTooltipPositioner tooltipPositioner, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, tooltip, tooltipPositioner, mouseX, mouseY);
	}
	
	public void renderTooltip(Font font, ItemStack stack, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, stack, mouseX, mouseY);
	}
	
	public void renderTooltip(Font font, Component tooltip, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, tooltip, mouseX, mouseY);
	}
	
	public void renderTooltip(Font font, List<Component> tooltip, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY)
	{
		gfx.renderTooltip(font, tooltip, visualTooltipComponent, mouseX, mouseY);
	}
	
	public void drawCenteredString(Font font, Component text, int x, int y, int color)
	{
		gfx.drawCenteredString(font, text, x, y, color);
	}
	
	public void drawCenteredString(Font font, String text, int x, int y, int color)
	{
		gfx.drawCenteredString(font, text, x, y, color);
	}
	
	public void drawCenteredString(Font font, FormattedCharSequence text, int x, int y, int color)
	{
		gfx.drawCenteredString(font, text, x, y, color);
	}
	
	public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color)
	{
		gfx.drawWordWrap(font, text, x, y, lineWidth, color);
	}
	
	public void renderOutline(int x, int y, int width, int height, int color)
	{
		gfx.renderOutline(x, y, width, height, color);
	}
	
	public void renderFakeItem(ItemStack stack, int x, int y)
	{
		gfx.renderFakeItem(stack, x, y);
	}
	
	public int getColorFromFormattingCharacter(char c, boolean isLighter)
	{
		return gfx.getColorFromFormattingCharacter(c, isLighter);
	}
	
	public void blitNineSlicedSized(ResourceLocation tex, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int uOffset, int vOffset, int texWidth, int texHeight)
	{
		gfx.blitNineSlicedSized(tex, x, y, width, height, sliceWidth, sliceHeight, uWidth, vHeight, uOffset, vOffset, texWidth, texHeight);
	}
	
	public void blitNineSlicedSized(ResourceLocation tex, int x, int y, int width, int height, int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight, int uWidth, int vHeight, int uOffset, int vOffset, int texWidth, int texHeight)
	{
		gfx.blitNineSlicedSized(tex, x, y, width, height, cornerWidth, cornerHeight, edgeWidth, edgeHeight, uWidth, vHeight, uOffset, vOffset, texWidth, texHeight);
	}
	
	public void blitNineSlicedSized(ResourceLocation tex, int x, int y, int width, int height, int sliceSize, int uWidth, int vHeight, int uOffset, int vOffset, int texWidth, int texHeight)
	{
		gfx.blitNineSlicedSized(tex, x, y, width, height, sliceSize, uWidth, vHeight, uOffset, vOffset, texWidth, texHeight);
	}
	
	public void blitWithBorder(ResourceLocation tex, int x, int y, int u, int v, int width, int height, int texWidth, int texHeight, int borderSize)
	{
		gfx.blitWithBorder(tex, x, y, u, v, width, height, texWidth, texHeight, borderSize);
	}
	
	public void blitWithBorder(ResourceLocation tex, int x, int y, int u, int v, int width, int height, int texWidth, int texHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder)
	{
		gfx.blitWithBorder(tex, x, y, u, v, width, height, texWidth, texHeight, topBorder, bottomBorder, leftBorder, rightBorder);
	}
	
	public void blitInscribed(ResourceLocation tex, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight)
	{
		gfx.blitInscribed(tex, x, y, boundsWidth, boundsHeight, rectWidth, rectHeight);
	}
	
	public void blitInscribed(ResourceLocation tex, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight, boolean centerX, boolean centerY)
	{
		gfx.blitInscribed(tex, x, y, boundsWidth, boundsHeight, rectWidth, rectHeight, centerX, centerY);
	}
}