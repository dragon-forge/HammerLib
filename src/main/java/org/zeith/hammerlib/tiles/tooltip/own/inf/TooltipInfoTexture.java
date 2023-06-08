package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;
import org.zeith.hammerlib.util.colors.ColorHelper;

public class TooltipInfoTexture
		implements IRenderableInfo
{
	public ResourceLocation texture;
	public float width, height;
	
	public float red, green, blue, alpha;
	
	public TooltipInfoTexture(ResourceLocation texture, float width, float height, int rgba)
	{
		this.texture = texture;
		this.width = width;
		this.height = height;
		
		this.red = ColorHelper.getRed(rgba);
		this.green = ColorHelper.getGreen(rgba);
		this.blue = ColorHelper.getBlue(rgba);
		this.alpha = ColorHelper.getAlpha(rgba);
	}
	
	@Override
	public float getWidth()
	{
		return this.width;
	}
	
	@Override
	public float getHeight()
	{
		return this.height;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(GuiGraphics matrix, float x, float y, float partialTime)
	{
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(red, green, blue, alpha);
		FXUtils.bindTexture(texture);
		RenderUtils.drawTexturedModalRect(matrix.pose(), x, y, null, width, height);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}
}