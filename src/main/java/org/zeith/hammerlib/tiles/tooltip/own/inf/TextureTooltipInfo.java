package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class TextureTooltipInfo
		implements IRenderableInfo
{
	public ResourceLocation texture;
	public int width, height;

	public TextureTooltipInfo(ResourceLocation texture, int width, int height)
	{
		this.texture = texture;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	public void render(MatrixStack matrix, float x, float y, float partialTime)
	{
		Minecraft.getInstance().getTextureManager().bind(texture);
		RenderUtils.drawTexturedModalRect(x, y, null, width, height);
	}
}