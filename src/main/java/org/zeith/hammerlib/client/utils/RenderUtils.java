package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.lwjgl.opengl.GL11;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.itf.IntToIntFunction;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class RenderUtils
{
	public static float zLevel = 0;

	public static TextureAtlasSprite getMainSprite(ResourceLocation tex)
	{
		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tex);
	}

	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height)
	{
		float n = 0.00390625F;
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.vertex(x, y + height, zLevel).uv((float) texX * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y + height, zLevel).uv((float) (texX + width) * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y, zLevel).uv((float) (texX + width) * n, (float) texY * n).endVertex();
		vb.vertex(x, y, zLevel).uv((float) texX * n, (float) texY * n).endVertex();
		tess.end();
	}

	public static void drawTexturedModalRect(PoseStack pose, float x, float y, float texX, float texY, float width, float height)
	{
		Matrix4f pose4f = pose.last().pose();
		float n = 0.00390625F;
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.vertex(pose4f, x, y + height, zLevel).uv(texX * n, (texY + height) * n).endVertex();
		vb.vertex(pose4f, x + width, y + height, zLevel).uv((texX + width) * n, (texY + height) * n).endVertex();
		vb.vertex(pose4f, x + width, y, zLevel).uv((texX + width) * n, texY * n).endVertex();
		vb.vertex(pose4f, x, y, zLevel).uv(texX * n, texY * n).endVertex();
		tess.end();
	}

	public static void drawFullTexturedModalRect(double x, double y, double width, double height)
	{
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.vertex(x, y + height, zLevel).uv(0, 1).endVertex();
		vb.vertex(x + width, y + height, zLevel).uv(1, 1).endVertex();
		vb.vertex(x + width, y, zLevel).uv(1, 0).endVertex();
		vb.vertex(x, y, zLevel).uv(0, 0).endVertex();
		tess.end();
	}

	public static void drawFullTexturedModalRect(PoseStack pose, float x, float y, float width, float height)
	{
		Matrix4f pose4f = pose.last().pose();
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.vertex(pose4f, x, y + height, zLevel).uv(0, 1).endVertex();
		vb.vertex(pose4f, x + width, y + height, zLevel).uv(1, 1).endVertex();
		vb.vertex(pose4f, x + width, y, zLevel).uv(1, 0).endVertex();
		vb.vertex(pose4f, x, y, zLevel).uv(0, 0).endVertex();
		tess.end();
	}

	public static void drawColoredModalRect(double x, double y, double width, double height, int rgb)
	{
		float r = ColorHelper.getRed(rgb), g = ColorHelper.getGreen(rgb), b = ColorHelper.getBlue(rgb), a = ColorHelper.getAlpha(rgb);
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		vb.vertex(x, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x + width, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x + width, y, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x, y, zLevel).color(r, g, b, a).endVertex();
		tess.end();
	}

	public static void drawColoredModalRect(PoseStack pose, float x, float y, float width, float height, int rgb)
	{
		Matrix4f pose4f = pose.last().pose();
		float r = ColorHelper.getRed(rgb), g = ColorHelper.getGreen(rgb), b = ColorHelper.getBlue(rgb), a = ColorHelper.getAlpha(rgb);
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		vb.vertex(pose4f, x, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(pose4f, x + width, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(pose4f, x + width, y, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(pose4f, x, y, zLevel).color(r, g, b, a).endVertex();
		tess.end();
	}

	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height, double zLevel)
	{
		float n = 0.00390625F;
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.vertex(x, y + height, zLevel).uv((float) texX * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y + height, zLevel).uv((float) (texX + width) * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y, zLevel).uv((float) (texX + width) * n, (float) texY * n).endVertex();
		vb.vertex(x, y, zLevel).uv((float) texX * n, (float) texY * n).endVertex();
		tess.end();
	}

	public static void drawTexturedModalRect(double xCoord, double yCoord, @Nullable TextureAtlasSprite textureSprite, double widthIn, double heightIn)
	{
		float minU = textureSprite == null ? 0 : textureSprite.getU0();
		float minV = textureSprite == null ? 0 : textureSprite.getV0();
		float maxU = textureSprite == null ? 1 : textureSprite.getU1();
		float maxV = textureSprite == null ? 1 : textureSprite.getV1();

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vertexbuffer.vertex(xCoord, yCoord + heightIn, 0).uv(minU, maxV).endVertex();
		vertexbuffer.vertex(xCoord + widthIn, yCoord + heightIn, 0).uv(maxU, maxV).endVertex();
		vertexbuffer.vertex(xCoord + widthIn, yCoord, 0).uv(maxU, minV).endVertex();
		vertexbuffer.vertex(xCoord, yCoord, 0).uv(minU, minV).endVertex();
		tessellator.end();
	}

	public static void drawTexturedModalRect(PoseStack pose, float xCoord, float yCoord, @Nullable TextureAtlasSprite textureSprite, float widthIn, float heightIn)
	{
		Matrix4f pose4f = pose.last().pose();

		float minU = textureSprite == null ? 0 : textureSprite.getU0();
		float minV = textureSprite == null ? 0 : textureSprite.getV0();
		float maxU = textureSprite == null ? 1 : textureSprite.getU1();
		float maxV = textureSprite == null ? 1 : textureSprite.getV1();

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vertexbuffer.vertex(pose4f, xCoord, yCoord + heightIn, 0).uv(minU, maxV).endVertex();
		vertexbuffer.vertex(pose4f, xCoord + widthIn, yCoord + heightIn, 0).uv(maxU, maxV).endVertex();
		vertexbuffer.vertex(pose4f, xCoord + widthIn, yCoord, 0).uv(maxU, minV).endVertex();
		vertexbuffer.vertex(pose4f, xCoord, yCoord, 0).uv(minU, minV).endVertex();
		tessellator.end();
	}

	public static void drawGradientRect(double left, double top, double width, double height, int startColor, int endColor)
	{
		float f = (startColor >> 24 & 255) / 255F;
		float f1 = (startColor >> 16 & 255) / 255F;
		float f2 = (startColor >> 8 & 255) / 255F;
		float f3 = (startColor & 255) / 255F;
		float f4 = (endColor >> 24 & 255) / 255F;
		float f5 = (endColor >> 16 & 255) / 255F;
		float f6 = (endColor >> 8 & 255) / 255F;
		float f7 = (endColor & 255) / 255F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		vertexbuffer.vertex(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.vertex(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static void drawGradientRect(double left, double top, double width, double height, int startColor, int endColor, double zLevel)
	{
		float f = (startColor >> 24 & 255) / 255F;
		float f1 = (startColor >> 16 & 255) / 255F;
		float f2 = (startColor >> 8 & 255) / 255F;
		float f3 = (startColor & 255) / 255F;
		float f4 = (endColor >> 24 & 255) / 255F;
		float f5 = (endColor >> 16 & 255) / 255F;
		float f6 = (endColor >> 8 & 255) / 255F;
		float f7 = (endColor & 255) / 255F;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		vb.vertex(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vb.vertex(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vb.vertex(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vb.vertex(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tess.end();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static void drawTextRGBA(Font font, PoseStack stack, String s, int x, int y, int r, int g, int b, int a)
	{
		font.draw(stack, s, x, y, ColorHelper.packARGB(r, g, b, a));
	}

	public static void drawTextRGBA(Font font, PoseStack stack, Component s, int x, int y, int r, int g, int b, int a)
	{
		font.draw(stack, s, x, y, ColorHelper.packARGB(r, g, b, a));
	}

	public static void drawLine(Vector3d start, Vector3d end, int color, float size)
	{
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		ColorHelper.glColor1ia(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(start.x, start.y, start.z);
		GL11.glVertex3d(end.x, end.y, end.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		RenderSystem.enableTexture();
		ColorHelper.glColor1ia(0xFFFFFFFF);
	}

	public static void drawBrokenLine(int color, float size, Vector3d... points)
	{
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		ColorHelper.glColor1ia(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		for(Vector3d point : points)
			GL11.glVertex3d(point.x, point.y, point.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		RenderSystem.enableTexture();
		ColorHelper.glColor1ia(0xFFFFFFFF);
	}

	private static final Random rand = new Random();

	public static void renderColorfulLightRayEffects(MultiBufferSource buffers, PoseStack matrix, IntToIntFunction rgba, long seed, float progress, int dstJump, int rayCount)
	{
		renderColorfulLightRayEffects(buffers, matrix, rgba, seed, progress, dstJump, 1.0F, rayCount);
	}

	public static void renderLightRayEffects(MultiBufferSource buffers, PoseStack matrix, int rgba, long seed, float progress, int dstJump, int rayCount)
	{
		renderLightRayEffects(buffers, matrix, rgba, seed, progress, dstJump, 1.0F, rayCount);
	}

	public static void setGlClearColorFromInt(int colorValue, int opacity)
	{
		int i = (colorValue & 16711680) >> 16;
		int j = (colorValue & 65280) >> 8;
		int k = (colorValue & 255);
		RenderSystem.clearColor(i / 255.0f, j / 255.0f, k / 255.0f, opacity / 255.0f);
	}

	public static void renderLightRayEffects(MultiBufferSource buffers, PoseStack matrix, int rgba, long seed, float progress, int dstJump, float scale, int rayCount)
	{
		rand.setSeed(seed);
		float dstJumpScale = 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
		float f7 = Math.min(progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F, 1.0F);

		int r = (rgba >> 16) & 0xFF;
		int g = (rgba >> 8) & 0xFF;
		int b = rgba & 0xFF;
		int a = (rgba >> 24) & 0xFF;

		VertexConsumer VertexConsumer2 = buffers.getBuffer(RenderType.lightning());
		matrix.pushPose();
		for(int i = 0; i < rayCount; i++)
		{
			matrix.mulPose(Vector3f.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(rand.nextFloat() * 360.0F + progress * 360.0F));

			float f3 = rand.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
			f3 /= dstJumpScale;
			f4 /= dstJumpScale;
			Matrix4f mat = matrix.last().pose();

			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex2(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex3(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex3(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex4(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex4(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex2(VertexConsumer2, mat, f3, f4, r, g, b);
		}
		matrix.popPose();
	}

	public static void renderColorfulLightRayEffects(MultiBufferSource buffers, PoseStack matrix, IntToIntFunction rgba, long seed, float progress, int dstJump, float scale, int rayCount)
	{
		rand.setSeed(seed);
		float dstJumpScale = 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
		float f7 = Math.min(progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F, 1.0F);

		VertexConsumer VertexConsumer2 = buffers.getBuffer(RenderType.lightning());
		matrix.pushPose();
		for(int i = 0; i < rayCount; i++)
		{
			int irgba = rgba.applyAsInt(i);

			int r = (irgba >> 16) & 0xFF;
			int g = (irgba >> 8) & 0xFF;
			int b = irgba & 0xFF;
			int a = (irgba >> 24) & 0xFF;

			matrix.mulPose(Vector3f.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(rand.nextFloat() * 360.0F + progress * 360.0F));

			float f3 = rand.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
			f3 /= dstJumpScale;
			f4 /= dstJumpScale;
			Matrix4f mat = matrix.last().pose();

			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex2(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex3(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex3(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex4(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex01(VertexConsumer2, mat, r, g, b, a);
			vertex4(VertexConsumer2, mat, f3, f4, r, g, b);
			vertex2(VertexConsumer2, mat, f3, f4, r, g, b);
		}
		matrix.popPose();
	}

	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

	private static void vertex01(VertexConsumer p_229061_0_, Matrix4f p_229061_1_, int r, int g, int b, int a)
	{
		p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(r, g, b, a).endVertex();
		p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(r, g, b, a).endVertex();
	}

	private static void vertex2(VertexConsumer p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_, int r, int g, int b)
	{
		p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(r, g, b, 0).endVertex();
	}

	private static void vertex3(VertexConsumer p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_, int r, int g, int b)
	{
		p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(r, g, b, 0).endVertex();
	}

	private static void vertex4(VertexConsumer p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_, int r, int g, int b)
	{
		p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, p_229063_3_).color(r, g, b, 0).endVertex();
	}

	public static void drawHorizontalGradientRect(float left, float top, float width, float height, int startColor, int endColor)
	{
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;

		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;

		float right = left + width;
		float bottom = top + height;

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		bufferbuilder.vertex(right, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.vertex(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static void drawVerticalGradientRect(float left, float top, float width, float height, int startColor, int endColor)
	{
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;

		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;

		float right = left + width;
		float bottom = top + height;

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		bufferbuilder.vertex(right, top, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.vertex(left, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, bottom, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

	public static void drawRect(PoseStack pose, int x, int y, int width, int height, int color)
	{
		RenderSystem.disableTexture();
		drawColoredModalRect(pose, x, y, width, height, color);
		RenderSystem.enableTexture();
	}

	public static class PlayerRenderUtil
	{
		public static void rotateIfSneaking(Player player)
		{
			if(player.isShiftKeyDown())
				applySneakingRotation();
		}

		public static void applySneakingRotation()
		{
			GL11.glTranslatef(0F, 0.2F, 0F);
			GL11.glRotatef(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}

		public static void translateToHeadLevel(Player player)
		{
			GL11.glTranslatef(0, -player.getEyeHeight(), 0);
			if(player.isShiftKeyDown())
				GL11.glTranslatef(0.25F * Mth.sin(player.getYRot() * (float) Math.PI / 180), 0.25F * Mth.cos(player.getYRot() * (float) Math.PI / 180), 0F);
		}

		public static void translateToFace()
		{
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(0f, -4.35f, -1.27f);
		}

		public static void defaultTransforms()
		{
			GL11.glTranslatef(0, 3, 1);
			GL11.glScalef(0.55F, 0.55F, 0.55F);
		}

		public static void translateToChest()
		{
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(0F, -3.2F, -0.85F);
		}
	}
}