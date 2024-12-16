package org.zeith.hammerlib.client.flowgui;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Builder
public record Graphics(GuiGraphics gfx, Minecraft game, float partialTime, boolean debugBounds)
{
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
	
	public void blit(ResourceLocation tex, int x, int y, float uOffset, float vOffset, int uWidth, int vHeight, int txWidth, int txHeight)
	{
		gfx.blit(RenderType::guiTexturedOverlay, tex, x, y, uOffset, vOffset, uWidth, vHeight, txWidth, txHeight);
	}
	
	public void blit(ResourceLocation tex, int x, int y, float uOffset, float vOffset, int uWidth, int vHeight, int txWidth, int txHeight, int color)
	{
		gfx.blit(RenderType::guiTexturedOverlay, tex, x, y, uOffset, vOffset, uWidth, vHeight, txWidth, txHeight, color);
	}
	
	public void blit(ResourceLocation tex, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight)
	{
		blit(RenderType::guiTexturedOverlay, tex, x, y, uOffset, vOffset, uWidth, vHeight, 256, 256);
	}
	
	public void blitFull(ResourceLocation tex, float x, float y, float width, float height, int color)
	{
		blitFull(RenderType::guiTexturedOverlay, tex, x, y, width, height, color);
	}
	
	public void blitFull(Function<ResourceLocation, RenderType> pRenderTypeGetter, ResourceLocation tex, float x, float y, float width, float height, int color)
	{
		blit(pRenderTypeGetter, tex, x, y, 0, 0, width, height, width, height, width, height, color);
	}
	
	public void blit(
			Function<ResourceLocation, RenderType> pRenderTypeGetter,
			ResourceLocation pAtlasLocation,
			float pX,
			float pY,
			float pUOffset,
			float pVOffset,
			float pUWidth,
			float pVHeight,
			float pWidth,
			float pHeight,
			float pTextureWidth,
			float pTextureHeight,
			int pColor
	)
	{
		rawBlit(
				pRenderTypeGetter,
				pAtlasLocation,
				pX,
				pX + pUWidth,
				pY,
				pY + pVHeight,
				(pUOffset + 0.0F) / pTextureWidth,
				(pUOffset + pWidth) / pTextureWidth,
				(pVOffset + 0.0F) / pTextureHeight,
				(pVOffset + pHeight) / pTextureHeight,
				pColor
		);
	}
	
	public void rawBlit(
			Function<ResourceLocation, RenderType> pRenderTypeGetter,
			ResourceLocation pAtlasLocation,
			float pX1,
			float pX2,
			float pY1,
			float pY2,
			float pMinU,
			float pMaxU,
			float pMinV,
			float pMaxV,
			int pColor
	)
	{
		drawSpecial(mbs ->
		{
			var type = pRenderTypeGetter.apply(pAtlasLocation);
			var matrix4f = gfx.pose().last().pose();
			var vertCons = mbs.getBuffer(type);
			vertCons.addVertex(matrix4f, pX1, pY1, 0.0F).setUv(pMinU, pMinV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX1, pY2, 0.0F).setUv(pMinU, pMaxV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX2, pY2, 0.0F).setUv(pMaxU, pMaxV).setColor(pColor);
			vertCons.addVertex(matrix4f, pX2, pY1, 0.0F).setUv(pMaxU, pMinV).setColor(pColor);
		});
	}
	
	public void drawSpecial(Consumer<MultiBufferSource> drawer)
	{
		gfx.drawSpecial(drawer);
	}
	
	public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite)
	{
		gfx.blitSprite(RenderType::guiTexturedOverlay, sprite, x, y, width, height, z);
	}
	
	public void blit(TextureAtlasSprite sprite, int x, int y, int z, int width, int height)
	{
		gfx.blitSprite(RenderType::guiTexturedOverlay, sprite, x, y, width, height, z);
	}
	
	public void blit(int x, int y, int width, int height, TextureAtlasSprite sprite)
	{
		gfx.blitSprite(RenderType::guiTexturedOverlay, sprite, x, y, width, height);
	}
	
	public void blit(TextureAtlasSprite sprite, int x, int y, int width, int height)
	{
		gfx.blitSprite(RenderType::guiTexturedOverlay, sprite, x, y, width, height);
	}
	
	public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite, float red, float green, float blue, float alpha)
	{
		gfx.blitSprite(RenderType::guiTexturedOverlay, sprite, x, y, z, width, ARGB.colorFromFloat(red, green, blue, alpha));
	}
	
	public void blit(ResourceLocation tex, float x, float y, float width, float height, float uOffset, float vOffset, float uWidth, float vHeight, int texWidth, int texHeight)
	{
		blit(RenderType::guiTexturedOverlay, tex, x, y, uOffset, vOffset, uWidth, vHeight, width, height, texWidth, texHeight, ARGB.white(1F));
	}
	
	public void blit(ResourceLocation tex, float x, float y, float width, float height, float uOffset, float vOffset, float uWidth, float vHeight, int texWidth, int texHeight, int color)
	{
		blit(RenderType::guiTexturedOverlay, tex, x, y, uOffset, vOffset, uWidth, vHeight, width, height, texWidth, texHeight, color);
	}
	
	public void blit(Function<ResourceLocation, RenderType> renderType, ResourceLocation tex, float x, float y, float uOffset, float vOffset, float width, float height, int txWidth, int txHeight)
	{
		blit(renderType, tex, x, y, uOffset, vOffset, width, height, width, height, txWidth, txHeight, ARGB.white(1F));
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
	
	public void blitInscribed(ResourceLocation tex, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight)
	{
		gfx.blitInscribed(tex, x, y, boundsWidth, boundsHeight, rectWidth, rectHeight);
	}
	
	public void blitInscribed(ResourceLocation tex, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight, boolean centerX, boolean centerY)
	{
		gfx.blitInscribed(tex, x, y, boundsWidth, boundsHeight, rectWidth, rectHeight, centerX, centerY);
	}
}