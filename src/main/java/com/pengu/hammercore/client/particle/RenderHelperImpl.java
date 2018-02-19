package com.pengu.hammercore.client.particle;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public enum RenderHelperImpl implements iRenderHelper
{
	INSTANCE;
	
	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final Random ENDFX_RANDOM = new Random(31100L);
	private static final FloatBuffer ENDFX_MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer ENDFX_PROJECTION = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer ENDFX_buffer = GLAllocation.createDirectFloatBuffer(16);
	
	private FloatBuffer getEndFXBuffer(float f0, float f1, float f2, float f3)
	{
		ENDFX_buffer.clear();
		ENDFX_buffer.put(f0).put(f1).put(f2).put(f3);
		ENDFX_buffer.flip();
		return ENDFX_buffer;
	}
	
	@Override
	public void renderEndPortalEffect(double x, double y, double z, ResourceLocation end_portal_texture, EnumFacing... renderSides)
	{
		GlStateManager.disableFog();
		
		GlStateManager.disableLighting();
		ENDFX_RANDOM.setSeed(31100L);
		GlStateManager.getFloat(2982, ENDFX_MODELVIEW);
		GlStateManager.getFloat(2983, ENDFX_PROJECTION);
		
		double d = x * x + y * y + z * z;
		byte b;
		if(d > 36864D)
			b = 2;
		else if(d > 25600D)
			b = 4;
		else if(d > 16384D)
			b = 6;
		else if(d > 9216D)
			b = 8;
		else if(d > 4096D)
			b = 10;
		else if(d > 1024D)
			b = 12;
		else if(d > 576D)
			b = 14;
		else if(d > 256D)
			b = 15;
		else
			b = 16;
		
		for(int i = 0; i < b; ++i)
		{
			GlStateManager.pushMatrix();
			
			float colorFactor = 2.0F / (18 - i);
			
			if(i == 0)
			{
				Minecraft.getMinecraft().renderEngine.bindTexture(END_SKY_TEXTURE);
				colorFactor = 0.15F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}
			
			if(i >= 1)
				Minecraft.getMinecraft().renderEngine.bindTexture(end_portal_texture);
			
			if(i == 1)
			{
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			}
			
			GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9474, getEndFXBuffer(1F, 0F, 0F, 0F));
			GlStateManager.texGen(GlStateManager.TexGen.T, 9474, getEndFXBuffer(0F, 1F, 0F, 0F));
			GlStateManager.texGen(GlStateManager.TexGen.R, 9474, getEndFXBuffer(0F, 0F, 1F, 0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.scale(0.5F, 0.5F, 1.0F);
			float RNT = i + 1;
			GlStateManager.translate(17.0F / RNT, (2.0F + RNT / 1.5F) * (Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
			GlStateManager.rotate((RNT * RNT * 4321.0F + RNT * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(4.5F - RNT / 4.0F, 4.5F - RNT / 4.0F, 1.0F);
			GlStateManager.multMatrix(ENDFX_PROJECTION);
			GlStateManager.multMatrix(ENDFX_MODELVIEW);
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder vb = tess.getBuffer();
			
			vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
			float red = (ENDFX_RANDOM.nextFloat() * 0.5F + 0.1F) * colorFactor;
			float green = (ENDFX_RANDOM.nextFloat() * 0.5F + 0.4F) * colorFactor;
			float blue = (ENDFX_RANDOM.nextFloat() * 0.5F + 0.5F) * colorFactor;
			
			for(EnumFacing f : renderSides)
			{
				if(f == EnumFacing.SOUTH)
				{
					vb.pos(x, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
				}
				
				if(f == EnumFacing.NORTH)
				{
					vb.pos(x, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y, z).color(red, green, blue, 1.0F).endVertex();
				}
				
				if(f == EnumFacing.EAST)
				{
					vb.pos(x + 1.0D, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z).color(red, green, blue, 1.0F).endVertex();
				}
				
				if(f == EnumFacing.WEST)
				{
					vb.pos(x, y, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
				}
				
				if(f == EnumFacing.DOWN)
				{
					vb.pos(x, y, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
				}
				
				if(f == EnumFacing.UP)
				{
					vb.pos(x, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y + 1.0D, z + 1.0D).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x + 1.0D, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
					vb.pos(x, y + 1.0D, z).color(red, green, blue, 1.0F).endVertex();
				}
			}
			
			tess.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			Minecraft.getMinecraft().renderEngine.bindTexture(END_SKY_TEXTURE);
		}
		
		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.enableLighting();
		GlStateManager.enableFog();
	}
}