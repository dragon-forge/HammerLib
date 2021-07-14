package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderableInfo
{
	int getWidth();

	int getHeight();

	@OnlyIn(Dist.CLIENT)
	void render(MatrixStack matrix, float x, float y, float partialTime);
}