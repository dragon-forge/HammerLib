package org.zeith.hammerlib.tiles.tooltip.own;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderableInfo
{
	float getWidth();
	
	float getHeight();
	
	@OnlyIn(Dist.CLIENT)
	void render(PoseStack matrix, float x, float y, float partialTime);
}