package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.java.DirectStorage;

public class GuiImageObject
		extends GuiObject
{
	public ResourceLocation tex;
	public float uOffset, vOffset;
	public float imgWidth, imgHeight, txWidth, txHeight;
	
	public Vec3 color = Vec3.fromRGB24(0xFFFFFFFF);
	public float alpha = 1F;
	
	public final DirectStorage<Float> textureUOffset = DirectStorage.create(p -> uOffset = p, () -> uOffset);
	public final DirectStorage<Float> textureVOffset = DirectStorage.create(p -> vOffset = p, () -> vOffset);
	
	public final DirectStorage<Float> imageWidth = DirectStorage.create(p -> this.width = imgWidth = p, () -> imgWidth);
	public final DirectStorage<Float> imageHeight = DirectStorage.create(p -> this.height = imgHeight = p, () -> imgHeight);
	
	public final DirectStorage<Float> fileWidth = DirectStorage.create(p -> txWidth = p, () -> txWidth);
	public final DirectStorage<Float> fileHeight = DirectStorage.create(p -> txHeight = p, () -> txHeight);
	
	public GuiImageObject(
			String name,
			ResourceLocation tex,
			float uOffset, float vOffset,
			float imgWidth, float imgHeight,
			float txWidth, float txHeight
	)
	{
		super(name);
		this.tex = tex;
		this.uOffset = uOffset;
		this.vOffset = vOffset;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.txWidth = txWidth;
		this.txHeight = txHeight;
		size(imgWidth, imgHeight);
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		gfx.drawManaged(() ->
		{
			RenderSystem.setShaderTexture(0, tex);
			blitWithBlend(gfx.gfx(), uOffset, vOffset, width, height, txWidth, txHeight, alpha, color);
		});
	}
	
	public static void blitWithBlend(
			GuiGraphics matrices,
			float texPosX, float texPosY,
			float width, float height,
			float texWidth, float texHeight,
			float alpha, Vec3 rgb
	)
	{
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder vertex = Tesselator.getInstance().getBuilder();
		float u1 = texPosX / texWidth;
		float u2 = (texPosX + width) / texWidth;
		float v1 = texPosY / texHeight;
		float v2 = (texPosY + height) / texHeight;
		Matrix4f m = matrices.pose().last().pose();
		if(vertex.building()) vertex.endOrDiscardIfEmpty();
		vertex.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		
		float r = (float) rgb.x(), g = (float) rgb.y(), b = (float) rgb.z();
		vertex.vertex(m, 0, 0, 0).color(r, g, b, alpha).uv(u1, v1).endVertex();
		vertex.vertex(m, 0, height, 0).color(r, g, b, alpha).uv(u1, v2).endVertex();
		vertex.vertex(m, width, height, 0).color(r, g, b, alpha).uv(u2, v2).endVertex();
		vertex.vertex(m, width, 0, 0).color(r, g, b, alpha).uv(u2, v1).endVertex();
		BufferUploader.drawWithShader(vertex.end());
		RenderSystem.disableBlend();
	}
}