package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UV
{
	public ResourceLocation path;
	public float posX, posY, width, height;

	public UV(ResourceLocation resource, float x, float y, float w, float h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
		path = resource;
	}

	@OnlyIn(Dist.CLIENT)
	public void render(PoseStack pose, double x, double y, float width, float height)
	{
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale((1F / this.width) * width, (1F / this.height) * height, 1F);
		render(pose, 0, 0);
		pose.popPose();
	}

	@OnlyIn(Dist.CLIENT)
	public void render(PoseStack pose, float x, float y)
	{
		RenderSystem.enableBlend();
		bindTexture();
		RenderUtils.drawTexturedModalRect(pose, x, y, posX, posY, width, height);
	}

	@OnlyIn(Dist.CLIENT)
	public void bindTexture()
	{
		FXUtils.bindTexture(path);
	}

	@Override
	public String toString()
	{
		return "UV{" +
				"path=" + path +
				", posX=" + posX +
				", posY=" + posY +
				", width=" + width +
				", height=" + height +
				'}';
	}
}