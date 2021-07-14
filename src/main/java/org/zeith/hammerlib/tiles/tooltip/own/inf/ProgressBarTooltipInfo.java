package org.zeith.hammerlib.tiles.tooltip.own.inf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.tiles.tooltip.ProgressBar;
import org.zeith.hammerlib.tiles.tooltip.own.IRenderableInfo;

public class ProgressBarTooltipInfo
		implements IRenderableInfo
{
	public ProgressBar bar;

	public ProgressBarTooltipInfo(ProgressBar bar)
	{
		this.bar = bar;
	}

	@Override
	public int getWidth()
	{
		return 101;
	}

	@Override
	public int getHeight()
	{
		return 12;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float x, float y, float partialTime)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		RenderUtils.drawRect(0, 0, 101, 12, bar.borderColor);
		RenderUtils.drawRect(1, 1, 99, 10, bar.backgroundColor);

		int fill = bar.getProgressPercent();

		RenderUtils.drawRect(1, 1, fill, 10, bar.filledMainColor);

		for(int j = 0; j < fill; j += 2) RenderUtils.drawRect(1 + j, 1, 2, 10, bar.filledAlternateColor);

		Minecraft.getInstance().font.draw(matrix, (bar.prefix != null ? bar.prefix : "") + (bar.suffix != null ? bar.suffix : ""), 3, 2, 0xFFFFFF);
		GL11.glPopMatrix();
	}
}