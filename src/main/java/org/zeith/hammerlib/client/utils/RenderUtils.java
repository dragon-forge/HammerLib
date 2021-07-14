package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.itf.IntToIntFunction;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class RenderUtils
{
	public static double zLevel = 0;

	public static final Function<ResourceLocation, TextureAtlasSprite> spriteGetter = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS);

	public static TextureAtlasSprite getMainSprite(ResourceLocation tex)
	{
		return spriteGetter.apply(tex);
	}

	public static void drawFullRectangleFit(double x, double y, double width, double height)
	{
		int w = GLHelperHL.activeTextureWidth();
		int h = GLHelperHL.activeTextureHeight();

		float ws = 1, hs = 1;

		if(w > h)
			hs = h / (float) w;
		if(h > w)
			ws = w / (float) h;

		double nw = width * ws;
		double nh = height * hs;

		drawFullTexturedModalRect(x + (width - nw) / 2, y + (height - nh) / 2, nw, nh);
	}

	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.vertex(x, y + height, zLevel).uv((float) texX * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y + height, zLevel).uv((float) (texX + width) * n, (float) (texY + height) * n).endVertex();
		vb.vertex(x + width, y, zLevel).uv((float) (texX + width) * n, (float) texY * n).endVertex();
		vb.vertex(x, y, zLevel).uv((float) texX * n, (float) texY * n).endVertex();
		tess.end();
	}

	public static void drawFullTexturedModalRect(double x, double y, double width, double height)
	{
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.vertex(x, y + height, zLevel).uv(0, 1).endVertex();
		vb.vertex(x + width, y + height, zLevel).uv(1, 1).endVertex();
		vb.vertex(x + width, y, zLevel).uv(1, 0).endVertex();
		vb.vertex(x, y, zLevel).uv(0, 0).endVertex();
		tess.end();
	}

	public static void drawColoredModalRect(double x, double y, double width, double height, int rgb)
	{
		float r = ColorHelper.getRed(rgb), g = ColorHelper.getGreen(rgb), b = ColorHelper.getBlue(rgb), a = ColorHelper.getAlpha(rgb);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		vb.vertex(x, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x + width, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x + width, y, zLevel).color(r, g, b, a).endVertex();
		vb.vertex(x, y, zLevel).color(r, g, b, a).endVertex();
		tess.end();
	}

	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height, double zLevel)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuilder();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
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

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.vertex(xCoord, yCoord + heightIn, 0).uv(minU, maxV).endVertex();
		vertexbuffer.vertex(xCoord + widthIn, yCoord + heightIn, 0).uv(maxU, maxV).endVertex();
		vertexbuffer.vertex(xCoord + widthIn, yCoord, 0).uv(maxU, minV).endVertex();
		vertexbuffer.vertex(xCoord, yCoord, 0).uv(minU, minV).endVertex();
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
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.vertex(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.vertex(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
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
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuilder();
		vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.vertex(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.vertex(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.vertex(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static void drawTextRGBA(FontRenderer font, MatrixStack stack, String s, int x, int y, int r, int g, int b, int a)
	{
		font.draw(stack, s, x, y, ColorHelper.packARGB(r, g, b, a));
	}

	public static void drawTextRGBA(FontRenderer font, MatrixStack stack, ITextComponent s, int x, int y, int r, int g, int b, int a)
	{
		font.draw(stack, s, x, y, ColorHelper.packARGB(r, g, b, a));
	}

	public static void drawLine(Vector3d start, Vector3d end, int color, float size)
	{
		RenderSystem.enableAlphaTest();
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
		RenderSystem.enableAlphaTest();
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

	public static void renderColorfulLightRayEffects(IRenderTypeBuffer buffers, MatrixStack matrix, IntToIntFunction rgba, long seed, float progress, int dstJump, int rayCount)
	{
		renderColorfulLightRayEffects(buffers, matrix, rgba, seed, progress, dstJump, 1.0F, rayCount);
	}

	public static void renderLightRayEffects(IRenderTypeBuffer buffers, MatrixStack matrix, int rgba, long seed, float progress, int dstJump, int rayCount)
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

	public static void renderLightRayEffects(IRenderTypeBuffer buffers, MatrixStack matrix, int rgba, long seed, float progress, int dstJump, float scale, int rayCount)
	{
		rand.setSeed(seed);
		float dstJumpScale = 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
		float f7 = Math.min(progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F, 1.0F);

		int r = (rgba >> 16) & 0xFF;
		int g = (rgba >> 8) & 0xFF;
		int b = rgba & 0xFF;
		int a = (rgba >> 24) & 0xFF;

		IVertexBuilder ivertexbuilder2 = buffers.getBuffer(RenderType.lightning());
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

			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex2(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex3(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex3(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex4(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex4(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex2(ivertexbuilder2, mat, f3, f4, r, g, b);
		}
		matrix.popPose();
	}

	public static void renderColorfulLightRayEffects(IRenderTypeBuffer buffers, MatrixStack matrix, IntToIntFunction rgba, long seed, float progress, int dstJump, float scale, int rayCount)
	{
		rand.setSeed(seed);
		float dstJumpScale = 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
		float f7 = Math.min(progress > 0.8F ? (progress - 0.8F) / 0.2F : 0.0F, 1.0F);

		IVertexBuilder ivertexbuilder2 = buffers.getBuffer(RenderType.lightning());
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

			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex2(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex3(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex3(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex4(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex01(ivertexbuilder2, mat, r, g, b, a);
			vertex4(ivertexbuilder2, mat, f3, f4, r, g, b);
			vertex2(ivertexbuilder2, mat, f3, f4, r, g, b);
		}
		matrix.popPose();
	}

	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);

	private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int r, int g, int b, int a)
	{
		p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(r, g, b, a).endVertex();
		p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(r, g, b, a).endVertex();
	}

	private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_, int r, int g, int b)
	{
		p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(r, g, b, 0).endVertex();
	}

	private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_, int r, int g, int b)
	{
		p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(r, g, b, 0).endVertex();
	}

	private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_, int r, int g, int b)
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
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.vertex(right, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.vertex(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
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
		RenderSystem.disableAlphaTest();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.vertex(right, top, 0).color(f5, f6, f7, f4).endVertex();
		bufferbuilder.vertex(left, top, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(left, bottom, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.vertex(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
		tessellator.end();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static void drawRect(int x, int y, int width, int height, int color)
	{
		RenderSystem.disableTexture();
		drawColoredModalRect(x, y, width, height, color);
		RenderSystem.enableTexture();
	}

	public static class PlayerRenderUtil
	{
		public static void rotateIfSneaking(PlayerEntity player)
		{
			if(player.isShiftKeyDown())
				applySneakingRotation();
		}

		public static void applySneakingRotation()
		{
			GL11.glTranslatef(0F, 0.2F, 0F);
			GL11.glRotatef(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}

		public static void translateToHeadLevel(PlayerEntity player)
		{
			GL11.glTranslatef(0, -player.getEyeHeight(), 0);
			if(player.isShiftKeyDown())
				GL11.glTranslatef(0.25F * MathHelper.sin(player.yRot * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.yRot * (float) Math.PI / 180), 0F);
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