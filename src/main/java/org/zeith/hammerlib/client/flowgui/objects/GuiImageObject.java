package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.client.render.texture.GuiTexture;
import org.zeith.hammerlib.util.java.DirectStorage;

public class GuiImageObject
		extends GuiObject
{
	public GuiTexture tex;
	public float uOffset, vOffset;
	public float imgWidth, imgHeight, txWidth, txHeight;
	
	public Vec3 color = Vec3.fromRGB24(0xFFFFFFFF);
	public float alpha = 1F;
	
	public final DirectStorage<Float> textureUOffset = DirectStorage.create(p -> uOffset = p, () -> uOffset);
	public final DirectStorage<Float> textureVOffset = DirectStorage.create(p -> vOffset = p, () -> vOffset);
	
	public final DirectStorage<Float> imageWidth = DirectStorage.create(p -> imgWidth = p, () -> imgWidth);
	public final DirectStorage<Float> imageHeight = DirectStorage.create(p -> imgHeight = p, () -> imgHeight);
	
	public final DirectStorage<Float> fileWidth = DirectStorage.create(p -> txWidth = p, () -> txWidth);
	public final DirectStorage<Float> fileHeight = DirectStorage.create(p -> txHeight = p, () -> txHeight);
	
	public GuiImageObject(
			String name,
			GuiTexture tex,
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
		gfx.drawSpecial((g) ->
		{
			RenderSystem.setShaderTexture(0, tex.texture());
			blitWithBlend(CoreShaders.POSITION_TEX_COLOR, gfx.gfx(),
					uOffset, vOffset,
					width, height,
					imgWidth, imgHeight,
					txWidth, txHeight,
					alpha, color
			);
		});
//		var drawer = tex.with(gfx);
//		drawer.state().color = ARGB.colorFromFloat(alpha, (float) color.x, (float) color.y, (float) color.z);
//		drawer.blitSegment(0, 0, uOffset, vOffset, imgWidth, imgHeight, txWidth, txHeight);
	}
	
	public static void blitWithBlend(
			ShaderProgram shader,
			GuiGraphics gfx,
			float texPosX, float texPosY,
			float width, float height,
			float texWidth, float texHeight,
			float alpha, Vec3 rgb
	)
	{
		blitWithBlend(
				shader,
				gfx,
				texPosX, texPosY,
				width, height,
				width, height,
				texWidth, texHeight,
				alpha, rgb
		);
	}
	
	public static void blitWithBlend(
			ShaderProgram shader,
			GuiGraphics gfx,
			float texPosX, float texPosY,
			float renderWidth, float renderHeight,
			float spriteWidth, float spriteHeight,
			float texWidth, float texHeight,
			float alpha, Vec3 rgb
	)
	{
		float u1 = texPosX / texWidth;
		float u2 = (texPosX + spriteWidth) / texWidth;
		float v1 = texPosY / texHeight;
		float v2 = (texPosY + spriteHeight) / texHeight;
		
		var pose = gfx.pose().last().pose();
		
		BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		
		RenderSystem.enableBlend();
		RenderSystem.setShader(shader);
		
		float r = (float) rgb.x(), g = (float) rgb.y(), b = (float) rgb.z();
		
		buf.addVertex(pose, 0, 0, 0).setColor(r, g, b, alpha).setUv(u1, v1);
		buf.addVertex(pose, 0, renderHeight, 0).setColor(r, g, b, alpha).setUv(u1, v2);
		buf.addVertex(pose, renderWidth, renderHeight, 0).setColor(r, g, b, alpha).setUv(u2, v2);
		buf.addVertex(pose, renderWidth, 0, 0).setColor(r, g, b, alpha).setUv(u2, v1);
		
		BufferUploader.drawWithShader(buf.build());
		RenderSystem.disableBlend();
	}
}