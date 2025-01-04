package org.zeith.hammerlib.client.flowgui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.zeith.hammerlib.client.flowgui.*;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.function.Supplier;

public class GuiImageObject
		extends GuiObject
{
	public Supplier<ResourceLocation> tex;
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
	
	public int shaderTexture = 0;
	public Supplier<ShaderInstance> shader = FlowguiShaderRegistry.GUI_SHADER;
	
	public GuiImageObject(
			String name,
			Supplier<ResourceLocation> tex,
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
	
	public GuiImageObject shader(Supplier<ShaderInstance> shader)
	{
		this.shader = shader;
		return this;
	}
	
	public GuiImageObject shaderTexture(int shaderTexture)
	{
		this.shaderTexture = shaderTexture;
		return this;
	}
	
	@Override
	protected void render(Graphics gfx, MousePos pos)
	{
		gfx.drawManaged(() ->
		{
			RenderSystem.setShaderTexture(shaderTexture, tex.get());
			blitWithBlend(shader, gfx.gfx(),
					uOffset, vOffset,
					width, height,
					imgWidth, imgHeight,
					txWidth, txHeight,
					alpha, color
			);
		});
	}
	
	public static void blitWithBlend(
			Supplier<ShaderInstance> shader,
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
			Supplier<ShaderInstance> shader,
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
		
		Matrix4f pose = gfx.pose().last().pose();
		
		BufferBuilder buf = Tesselator.getInstance().getBuilder();
		if(buf.building())
		{
			var batch = buf.endOrDiscardIfEmpty();
			if(batch != null) BufferUploader.drawWithShader(batch);
		}
		
		RenderSystem.enableBlend();
		RenderSystem.setShader(shader);
		buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
		
		float r = (float) rgb.x(), g = (float) rgb.y(), b = (float) rgb.z();
		
		buf.vertex(pose, 0, 0, 0).color(r, g, b, alpha).uv(u1, v1).endVertex();
		buf.vertex(pose, 0, renderHeight, 0).color(r, g, b, alpha).uv(u1, v2).endVertex();
		buf.vertex(pose, renderWidth, renderHeight, 0).color(r, g, b, alpha).uv(u2, v2).endVertex();
		buf.vertex(pose, renderWidth, 0, 0).color(r, g, b, alpha).uv(u2, v1).endVertex();
		
		BufferUploader.drawWithShader(buf.end());
		RenderSystem.disableBlend();
	}
}