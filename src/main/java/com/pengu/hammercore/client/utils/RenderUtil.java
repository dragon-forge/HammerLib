package com.pengu.hammercore.client.utils;

import java.util.Random;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.hammercore.vec.Vector3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtil
{
	public static double zLevel = 0;
	
	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y + height, zLevel).tex(texX * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y + height, zLevel).tex((texX + width) * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y, zLevel).tex((texX + width) * n, texY * n).endVertex();
		vb.pos(x, y, zLevel).tex(texX * n, texY * n).endVertex();
		tess.draw();
	}
	
	public static void drawColoredModalRect(double x, double y, double width, double height, int rgb)
	{
		float r = ColorHelper.getRed(rgb), g = ColorHelper.getGreen(rgb), b = ColorHelper.getBlue(rgb), a = ColorHelper.getAlpha(rgb);
		
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vb.pos(x, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.pos(x + width, y + height, zLevel).color(r, g, b, a).endVertex();
		vb.pos(x + width, y, zLevel).color(r, g, b, a).endVertex();
		vb.pos(x, y, zLevel).color(r, g, b, a).endVertex();
		tess.draw();
	}
	
	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height, double zLevel)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y + height, zLevel).tex(texX * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y + height, zLevel).tex((texX + width) * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y, zLevel).tex((texX + width) * n, texY * n).endVertex();
		vb.pos(x, y, zLevel).tex(texX * n, texY * n).endVertex();
		tess.draw();
	}
	
	public static void drawTexturedModalRect(double xCoord, double yCoord, TextureAtlasSprite textureSprite, double widthIn, double heightIn)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord, yCoord + heightIn, 0).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + heightIn, 0).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord, 0).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
		vertexbuffer.pos(xCoord, yCoord, 0).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
		tessellator.draw();
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
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
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
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public static void drawTextRGBA(FontRenderer font, String s, int x, int y, int r, int g, int b, int a)
	{
		font.drawString(s, x, y, Color.packARGB(r, g, b, a));
	}
	
	public static void drawLine(Vector3 start, Vector3 end, int color, float size)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		Color.glColourRGBA(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(start.x, start.y, start.z);
		GL11.glVertex3d(end.x, end.y, end.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		Color.glColourRGBA(0xFFFFFFFF);
	}
	
	public static void drawBrokenLine(int color, float size, Vector3... points)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		Color.glColourRGBA(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		for(Vector3 point : points)
			GL11.glVertex3d(point.x, point.y, point.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		Color.glColourRGBA(0xFFFFFFFF);
	}
	
	private static final Random rand = new Random();
	
	public static void renderColorfulLightRayEffects(double x, double y, double z, Function<Integer, Integer> rgba, long seed, float progress, int dstJump, int countFancy, int countNormal)
	{
		renderColorfulLightRayEffects(x, y, z, rgba, seed, progress, dstJump, 1.0F, countFancy, countNormal);
	}
	
	public static void renderLightRayEffects(double x, double y, double z, int rgba, long seed, float progress, int dstJump, int countFancy, int countNormal)
	{
		renderLightRayEffects(x, y, z, rgba, seed, progress, dstJump, 1.0F, countFancy, countNormal);
	}
	
	public static void renderLightRayEffects(double x, double y, double z, int rgba, long seed, float progress, int dstJump, float scale, int countFancy, int countNormal)
	{
		rand.setSeed(seed);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		int fc = Minecraft.getMinecraft().gameSettings.fancyGraphics ? countFancy : countNormal;
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();
		
		float r = ColorHelper.getRed(rgba);
		float g = ColorHelper.getGreen(rgba);
		float b = ColorHelper.getBlue(rgba);
		float a = ColorHelper.getAlpha(rgba);
		
		RenderHelper.disableStandardItemLighting();
		
		GL11.glDisable(3553);
		GL11.glShadeModel(7425);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		GL11.glDisable(3008);
		GL11.glDepthMask(false);
		GL11.glPushMatrix();
		
		for(int i = 0; i < fc; i++)
		{
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F + progress * 360F, 0.0F, 0.0F, 1.0F);
			vb.begin(6, DefaultVertexFormats.POSITION_COLOR);
			float fa = rand.nextFloat() * 20.0F + 5.0F + a * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + a * 2.0F;
			fa /= 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
			f4 /= 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
			vb.pos(0.0D, 0.0D, 0.0D).color(r, g, b, (int) (255.0F * a)).endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			vb.pos(0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			vb.pos(0.0D, fa, 1.0F * f4).color(r, g, b, 0).endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			tes.draw();
		}
		
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7424);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3553);
		GL11.glEnable(3008);
		RenderHelper.enableStandardItemLighting();
		
		GL11.glPopMatrix();
	}
	
	public static void renderColorfulLightRayEffects(double x, double y, double z, Function<Integer, Integer> rgba, long seed, float progress, int dstJump, float scale, int countFancy, int countNormal)
	{
		rand.setSeed(seed);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		
		int fc = Minecraft.getMinecraft().gameSettings.fancyGraphics ? countFancy : countNormal;
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();
		
		RenderHelper.disableStandardItemLighting();
		
		GL11.glDisable(3553);
		GL11.glShadeModel(7425);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		GL11.glDisable(3008);
		GL11.glDepthMask(false);
		GL11.glPushMatrix();
		
		for(int i = 0; i < fc; i++)
		{
			int irgba = rgba.apply(i);
			
			float r = ColorHelper.getRed(irgba);
			float g = ColorHelper.getGreen(irgba);
			float b = ColorHelper.getBlue(irgba);
			float a = ColorHelper.getAlpha(irgba);
			
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rand.nextFloat() * 360.0F + progress * 360F, 0.0F, 0.0F, 1.0F);
			vb.begin(6, DefaultVertexFormats.POSITION_COLOR);
			float fa = rand.nextFloat() * 20.0F + 5.0F + a * 10.0F;
			float f4 = rand.nextFloat() * 2.0F + 1.0F + a * 2.0F;
			fa /= 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
			f4 /= 30.0F / (Math.min(dstJump, 10.0F * scale) / 10.0F);
			vb.pos(0.0D, 0.0D, 0.0D).color(r, g, b, (int) (255.0F * a)).endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			vb.pos(0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			vb.pos(0.0D, fa, 1.0F * f4).color(r, g, b, 0).endVertex();
			vb.pos(-0.7D * f4, fa, -0.5F * f4).color(r, g, b, 0).endVertex();
			tes.draw();
		}
		
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3042);
		GL11.glShadeModel(7424);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3553);
		GL11.glEnable(3008);
		RenderHelper.enableStandardItemLighting();
		
		GL11.glPopMatrix();
	}
	
	public static class PlayerRenderUtil
	{
		
		public static void rotateIfSneaking(EntityPlayer player)
		{
			if(player.isSneaking())
				applySneakingRotation();
		}
		
		public static void applySneakingRotation()
		{
			GlStateManager.translate(0F, 0.2F, 0F);
			GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
		}
		
		public static void translateToHeadLevel(EntityPlayer player)
		{
			GlStateManager.translate(0, -player.getDefaultEyeHeight(), 0);
			if(player.isSneaking())
				GlStateManager.translate(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
		}
		
		public static void translateToFace()
		{
			GlStateManager.rotate(90F, 0F, 1F, 0F);
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0f, -4.35f, -1.27f);
		}
		
		public static void defaultTransforms()
		{
			GlStateManager.translate(0.0, 3.0, 1.0);
			GlStateManager.scale(0.55, 0.55, 0.55);
		}
		
		public static void translateToChest()
		{
			GlStateManager.rotate(180F, 1F, 0F, 0F);
			GlStateManager.translate(0F, -3.2F, -0.85F);
		}
		
	}
}