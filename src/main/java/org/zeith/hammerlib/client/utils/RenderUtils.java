package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.itf.IntToIntFunction;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderUtils
{
	public static float zLevel = 0;
	
	public static TextureAtlasSprite getMainSprite(ResourceLocation tex)
	{
		return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tex);
	}
	
	/**
	 * This renders the item into the GUI, with {@link PoseStack}, which is missing in vanilla render code for some reason...
	 */
	public static void renderItemIntoGui(PoseStack pose, ItemStack stack, int x, int y)
	{
		renderItemIntoGui(pose, stack, (float) x, y);
	}
	
	/**
	 * This renders the item into the GUI, with {@link PoseStack}, which is missing in vanilla render code for some reason...
	 */
	public static void renderItemIntoGui(PoseStack pose, ItemStack stack, float x, float y)
	{
		var mc = Minecraft.getInstance();
		var ir = mc.getItemRenderer();
		var tm = mc.getTextureManager();
		
		var p_115131_ = ir.getModel(stack, null, null, 0);
		
		tm.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		var posestack = RenderSystem.getModelViewStack();
		posestack.pushMatrix();
		
		// copy the given pose over to the model view.
		posestack.mul(pose.last().pose());
		
		posestack.translate(x, y, 100.0F);
		posestack.translate(8.0F, 8.0F, 0.0F);
		posestack.scale(1.0F, -1.0F, 1.0F);
		posestack.scale(16.0F, 16.0F, 16.0F);
		
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean flag = !p_115131_.usesBlockLight();
		if(flag)
		{
			Lighting.setupForFlatItems();
		}
		
		ir.render(stack, ItemDisplayContext.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, p_115131_);
		multibuffersource$buffersource.endBatch();
		RenderSystem.enableDepthTest();
		if(flag)
		{
			Lighting.setupFor3DItems();
		}
		
		posestack.popMatrix();
		RenderSystem.applyModelViewMatrix();
	}
	
	public static void drawTexturedModalRect(GuiGraphics pose, float x, float y, float texX, float texY, float width, float height)
	{
		drawTexturedModalRect(pose.pose(), x, y, texX, texY, width, height);
	}
	
	public static void drawTexturedModalRect(PoseStack pose, float x, float y, float texX, float texY, float width, float height)
	{
		Matrix4f pose4f = pose.last().pose();
		float n = 0.00390625F;
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.addVertex(pose4f, x, y + height, zLevel).setUv(texX * n, (texY + height) * n);
		vb.addVertex(pose4f, x + width, y + height, zLevel).setUv((texX + width) * n, (texY + height) * n);
		vb.addVertex(pose4f, x + width, y, zLevel).setUv((texX + width) * n, texY * n);
		vb.addVertex(pose4f, x, y, zLevel).setUv(texX * n, texY * n);
		BufferUploader.drawWithShader(vb.buildOrThrow());
	}
	
	public static void drawFullTexturedModalRect(GuiGraphics pose, float x, float y, float width, float height)
	{
		Matrix4f pose4f = pose.pose().last().pose();
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.addVertex(pose4f, x, y + height, zLevel).setUv(0, 1);
		vb.addVertex(pose4f, x + width, y + height, zLevel).setUv(1, 1);
		vb.addVertex(pose4f, x + width, y, zLevel).setUv(1, 0);
		vb.addVertex(pose4f, x, y, zLevel).setUv(0, 0);
		BufferUploader.drawWithShader(vb.buildOrThrow());
	}
	
	public static void drawColoredModalRect(GuiGraphics pose, float x, float y, float width, float height, int rgb)
	{
		Matrix4f pose4f = pose.pose().last().pose();
		float r = ColorHelper.getRed(rgb), g = ColorHelper.getGreen(rgb), b = ColorHelper.getBlue(rgb), a = ColorHelper.getAlpha(rgb);
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		vb.addVertex(pose4f, x, y + height, zLevel).setColor(r, g, b, a);
		vb.addVertex(pose4f, x + width, y + height, zLevel).setColor(r, g, b, a);
		vb.addVertex(pose4f, x + width, y, zLevel).setColor(r, g, b, a);
		vb.addVertex(pose4f, x, y, zLevel).setColor(r, g, b, a);
		BufferUploader.drawWithShader(vb.buildOrThrow());
	}
	
	public static void drawTexturedModalRect(PoseStack pose, float xCoord, float yCoord, @Nullable TextureAtlasSprite textureSprite, float widthIn, float heightIn)
	{
		Matrix4f pose4f = pose.last().pose();
		
		float minU;
		float minV;
		float maxU;
		float maxV;
		if(textureSprite == null)
		{
			minU = 0;
			minV = 0;
			maxU = 1;
			maxV = 1;
		} else
		{
			minU = textureSprite.getU0();
			minV = textureSprite.getV0();
			maxU = textureSprite.getU1();
			maxV = textureSprite.getV1();
		}
		
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder vb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		vb.addVertex(pose4f, xCoord, yCoord + heightIn, 0).setUv(minU, maxV);
		vb.addVertex(pose4f, xCoord + widthIn, yCoord + heightIn, 0).setUv(maxU, maxV);
		vb.addVertex(pose4f, xCoord + widthIn, yCoord, 0).setUv(maxU, minV);
		vb.addVertex(pose4f, xCoord, yCoord, 0).setUv(minU, minV);
		BufferUploader.drawWithShader(vb.buildOrThrow());
	}
	
	public static void drawTextRGBA(Font font, GuiGraphics stack, String s, int x, int y, int r, int g, int b, int a)
	{
		stack.drawString(font, s, x, y, ColorHelper.packARGB(r, g, b, a));
	}
	
	public static void drawTextRGBA(Font font, GuiGraphics stack, Component s, int x, int y, int r, int g, int b, int a)
	{
		stack.drawString(font, s, x, y, ColorHelper.packARGB(r, g, b, a));
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
			matrix.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0F + progress * 360.0F));
			
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
			
			matrix.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360.0F));
			matrix.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360.0F + progress * 360.0F));
			
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
	
	private static void vertex01(VertexConsumer c, Matrix4f pose, int r, int g, int b, int a)
	{
		c.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(r, g, b, a);
		c.addVertex(pose, 0.0F, 0.0F, 0.0F).setColor(r, g, b, a);
	}
	
	private static void vertex2(VertexConsumer c, Matrix4f pose, float p_229060_2_, float p_229060_3_, int r, int g, int b)
	{
		c.addVertex(pose, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).setColor(r, g, b, 0);
	}
	
	private static void vertex3(VertexConsumer c, Matrix4f pose, float p_229062_2_, float p_229062_3_, int r, int g, int b)
	{
		c.addVertex(pose, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).setColor(r, g, b, 0);
	}
	
	private static void vertex4(VertexConsumer c, Matrix4f pose, float p_229063_2_, float p_229063_3_, int r, int g, int b)
	{
		c.addVertex(pose, 0.0F, p_229063_2_, p_229063_3_).setColor(r, g, b, 0);
	}
	
	public static void drawRect(GuiGraphics pose, int x, int y, int width, int height, int color)
	{
		var sdr = RenderSystem.getShader();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		drawColoredModalRect(pose, x, y, width, height, color);
		RenderSystem.setShader(Cast.constant(sdr));
	}
	
	public static class PlayerRenderUtil
	{
		public static void rotateIfSneaking(PoseStack pose, Player player)
		{
			if(player.isShiftKeyDown())
				applySneakingRotation(pose);
		}
		
		public static void applySneakingRotation(PoseStack pose)
		{
			pose.translate(0F, 0.2F, 0F);
			pose.mulPose(Axis.XP.rotationDegrees(90F));
		}
		
		public static void translateToHeadLevel(PoseStack pose, Player player)
		{
			pose.translate(0, -player.getEyeHeight(), 0);
			if(player.isShiftKeyDown())
				pose.translate(0.25F * Mth.sin(player.getYRot() * (float) Math.PI / 180), 0.25F * Mth.cos(player.getYRot() * (float) Math.PI / 180), 0F);
		}
		
		public static void translateToFace(PoseStack pose)
		{
			pose.mulPose(Axis.YP.rotationDegrees(90F));
			pose.mulPose(Axis.XP.rotationDegrees(180F));
			pose.translate(0f, -4.35f, -1.27f);
		}
		
		public static void defaultTransforms(PoseStack pose)
		{
			pose.translate(0, 3, 1);
			pose.scale(0.55F, 0.55F, 0.55F);
		}
		
		public static void translateToChest(PoseStack pose)
		{
			pose.mulPose(Axis.XP.rotationDegrees(180F));
			pose.translate(0F, -3.2F, -0.85F);
		}
	}
}