package com.pengu.hammercore.client.utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlocks
{
	private static RenderBlocks instance;
	
	/**
	 * Replaced with instance alpha. This field is useless from now on. moved to
	 * field {@link #renderAlpha}
	 */
	@Deprecated
	public static float alpha = 1F;
	
	/**
	 * Use this to make your rendering very easy
	 */
	public final SimpleBlockRendering simpleRenderer = new SimpleBlockRendering(this);
	
	public float renderAlpha = 1F;
	public IBlockAccess blockAccess;
	public boolean flipTexture;
	public boolean field_152631_f;
	public boolean renderAllFaces;
	public boolean useInventoryTint = true;
	public boolean renderFromInside = false;
	public double renderMinX;
	public double renderMaxX;
	public double renderMinY;
	public double renderMaxY;
	public double renderMinZ;
	public double renderMaxZ;
	public boolean lockBlockBounds;
	public boolean partialRenderBounds;
	public final Minecraft minecraftRB;
	public int uvRotateEast;
	public int uvRotateWest;
	public int uvRotateSouth;
	public int uvRotateNorth;
	public int uvRotateTop;
	public int uvRotateBottom;
	public float aoLightValueScratchXYZNNN;
	public float aoLightValueScratchXYNN;
	public float aoLightValueScratchXYZNNP;
	public float aoLightValueScratchYZNN;
	public float aoLightValueScratchYZNP;
	public float aoLightValueScratchXYZPNN;
	public float aoLightValueScratchXYPN;
	public float aoLightValueScratchXYZPNP;
	public float aoLightValueScratchXYZNPN;
	public float aoLightValueScratchXYNP;
	public float aoLightValueScratchXYZNPP;
	public float aoLightValueScratchYZPN;
	public float aoLightValueScratchXYZPPN;
	public float aoLightValueScratchXYPP;
	public float aoLightValueScratchYZPP;
	public float aoLightValueScratchXYZPPP;
	public float aoLightValueScratchXZNN;
	public float aoLightValueScratchXZPN;
	public float aoLightValueScratchXZNP;
	public float aoLightValueScratchXZPP;
	public int aoBrightnessXYZNNN;
	public int aoBrightnessXYNN;
	public int aoBrightnessXYZNNP;
	public int aoBrightnessYZNN;
	public int aoBrightnessYZNP;
	public int aoBrightnessXYZPNN;
	public int aoBrightnessXYPN;
	public int aoBrightnessXYZPNP;
	public int aoBrightnessXYZNPN;
	public int aoBrightnessXYNP;
	public int aoBrightnessXYZNPP;
	public int aoBrightnessYZPN;
	public int aoBrightnessXYZPPN;
	public int aoBrightnessXYPP;
	public int aoBrightnessYZPP;
	public int aoBrightnessXYZPPP;
	public int aoBrightnessXZNN;
	public int aoBrightnessXZPN;
	public int aoBrightnessXZNP;
	public int aoBrightnessXZPP;
	public int brightnessTopLeft;
	public int brightnessBottomLeft;
	public int brightnessBottomRight;
	public int brightnessTopRight;
	public float colorRedTopLeft;
	public float colorRedBottomLeft;
	public float colorRedBottomRight;
	public float colorRedTopRight;
	public float colorGreenTopLeft;
	public float colorGreenBottomLeft;
	public float colorGreenBottomRight;
	public float colorGreenTopRight;
	public float colorBlueTopLeft;
	public float colorBlueBottomLeft;
	public float colorBlueBottomRight;
	public float colorBlueTopRight;
	
	public RenderBlocks(IBlockAccess p_i1251_1_)
	{
		blockAccess = p_i1251_1_;
		field_152631_f = false;
		flipTexture = false;
		minecraftRB = Minecraft.getMinecraft();
	}
	
	public RenderBlocks()
	{
		minecraftRB = Minecraft.getMinecraft();
	}
	
	public int setLighting(World world, BlockPos pos)
	{
		int i = world.getCombinedLight(pos, 0);
		for(EnumFacing f : EnumFacing.VALUES)
			i = Math.max(world.getCombinedLight(pos.offset(f), 0), i);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		return i;
	}
	
	public void setRenderBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		if(!lockBlockBounds)
		{
			renderMinX = minX;
			renderMaxX = maxX;
			renderMinY = minY;
			renderMaxY = maxY;
			renderMinZ = minZ;
			renderMaxZ = maxZ;
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0 || renderMaxX < 1 || renderMinY > 0 || renderMaxY < 1 || renderMinZ > 0 || renderMaxZ < 1);
		}
	}
	
	public void setRenderBoundsFromBlock(BlockPos pos, IBlockState state)
	{
		if(!lockBlockBounds)
		{
			AxisAlignedBB bb = state.getBoundingBox(blockAccess, pos);
			renderMinX = pos.getX() - bb.minX;
			renderMaxX = pos.getX() - bb.maxX;
			renderMinY = pos.getY() - bb.minY;
			renderMaxY = pos.getY() - bb.maxY;
			renderMinZ = pos.getZ() - bb.minZ;
			renderMaxZ = pos.getZ() - bb.maxZ;
			partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0 || renderMaxX < 1 || renderMinY > 0 || renderMaxY < 1 || renderMinZ > 0 || renderMaxZ < 1);
		}
	}
	
	public void overrideBlockBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		renderMinX = minX;
		renderMaxX = maxX;
		renderMinY = minY;
		renderMaxY = maxY;
		renderMinZ = minZ;
		renderMaxZ = maxZ;
		lockBlockBounds = true;
		partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0 || renderMaxX < 1 || renderMinY > 0 || renderMaxY < 1 || renderMinZ > 0 || renderMaxZ < 1);
	}
	
	public void renderFaceYNeg(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinX * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxX * 16D);
		double d5 = sprite.getInterpolatedV(renderMinZ * 16D);
		double d6 = sprite.getInterpolatedV(renderMaxZ * 16D);
		
		if(renderMinX < 0 || renderMaxX > 1)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinZ < 0D || renderMaxZ > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateBottom == 2)
		{
			d3 = sprite.getInterpolatedU(renderMinZ * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMaxX * 16D);
			d4 = sprite.getInterpolatedU(renderMaxZ * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMinX * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateBottom == 1)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxZ * 16D);
			d5 = sprite.getInterpolatedV(renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinZ * 16D);
			d6 = sprite.getInterpolatedV(renderMaxX * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateBottom == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxX * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMinZ * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMaxZ * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMinX;
		double d12 = x + renderMaxX;
		double d13 = y + renderMinY;
		double d14 = z + renderMinZ;
		double d15 = z + renderMaxZ;
		
		if(renderFromInside)
		{
			d11 = x + renderMaxX;
			d12 = x + renderMinX;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		tessellator.getBuffer().pos(d11, d13, d15).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d14).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d13, d14).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d13, d15).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFaceYPos(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinX * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxX * 16D);
		double d5 = sprite.getInterpolatedV(renderMinZ * 16D);
		double d6 = sprite.getInterpolatedV(renderMaxZ * 16D);
		
		if(renderMinX < 0D || renderMaxX > 1D)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinZ < 0D || renderMaxZ > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateTop == 1)
		{
			d3 = sprite.getInterpolatedU(renderMinZ * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMaxX * 16D);
			d4 = sprite.getInterpolatedU(renderMaxZ * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMinX * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateTop == 2)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxZ * 16D);
			d5 = sprite.getInterpolatedV(renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinZ * 16D);
			d6 = sprite.getInterpolatedV(renderMaxX * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateTop == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxX * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMinZ * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMaxZ * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMinX;
		double d12 = x + renderMaxX;
		double d13 = y + renderMaxY;
		double d14 = z + renderMinZ;
		double d15 = z + renderMaxZ;
		
		if(renderFromInside)
		{
			d11 = x + renderMaxX;
			d12 = x + renderMinX;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		tessellator.getBuffer().pos(d12, d13, d15).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d13, d14).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d14).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d15).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFaceZNeg(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinX * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxX * 16D);
		
		if(field_152631_f)
		{
			d4 = sprite.getInterpolatedU((1D - renderMinX) * 16D);
			d3 = sprite.getInterpolatedU((1D - renderMaxX) * 16D);
		}
		
		double d5 = sprite.getInterpolatedV(16D - renderMaxY * 16D);
		double d6 = sprite.getInterpolatedV(16D - renderMinY * 16D);
		
		if(flipTexture)
		{
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		
		if(renderMinX < 0D || renderMaxX > 1D)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinY < 0D || renderMaxY > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateEast == 2)
		{
			d3 = sprite.getInterpolatedU(renderMinY * 16D);
			d4 = sprite.getInterpolatedU(renderMaxY * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMinX * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMaxX * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateEast == 1)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxY * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinY * 16D);
			d5 = sprite.getInterpolatedV(renderMaxX * 16D);
			d6 = sprite.getInterpolatedV(renderMinX * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateEast == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxX * 16D);
			d5 = sprite.getInterpolatedV(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(renderMinY * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMinX;
		double d12 = x + renderMaxX;
		double d13 = y + renderMinY;
		double d14 = y + renderMaxY;
		double d15 = z + renderMinZ;
		
		if(renderFromInside)
		{
			d11 = x + renderMaxX;
			d12 = x + renderMinX;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		tessellator.getBuffer().pos(d11, d14, d15).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d14, d15).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d13, d15).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d15).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFaceZPos(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinX * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxX * 16D);
		double d5 = sprite.getInterpolatedV(16D - renderMaxY * 16D);
		double d6 = sprite.getInterpolatedV(16D - renderMinY * 16D);
		
		if(flipTexture)
		{
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		
		if(renderMinX < 0D || renderMaxX > 1D)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinY < 0D || renderMaxY > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateWest == 1)
		{
			d3 = sprite.getInterpolatedU(renderMinY * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMinX * 16D);
			d4 = sprite.getInterpolatedU(renderMaxY * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMaxX * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateWest == 2)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxY * 16D);
			d5 = sprite.getInterpolatedV(renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinY * 16D);
			d6 = sprite.getInterpolatedV(renderMaxX * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateWest == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinX * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxX * 16D);
			d5 = sprite.getInterpolatedV(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(renderMinY * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMinX;
		double d12 = x + renderMaxX;
		double d13 = y + renderMinY;
		double d14 = y + renderMaxY;
		double d15 = z + renderMaxZ;
		
		if(renderFromInside)
		{
			d11 = x + renderMaxX;
			d12 = x + renderMinX;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		tessellator.getBuffer().pos(d11, d14, d15).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d15).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d13, d15).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d12, d14, d15).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFaceXNeg(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinZ * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxZ * 16D);
		double d5 = sprite.getInterpolatedV(16D - renderMaxY * 16D);
		double d6 = sprite.getInterpolatedV(16D - renderMinY * 16D);
		
		if(flipTexture)
		{
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		
		if(renderMinZ < 0D || renderMaxZ > 1D)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinY < 0D || renderMaxY > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateNorth == 1)
		{
			d3 = sprite.getInterpolatedU(renderMinY * 16D);
			d5 = sprite.getInterpolatedV(16.0D - renderMaxZ * 16D);
			d4 = sprite.getInterpolatedU(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMinZ * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateNorth == 2)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxY * 16D);
			d5 = sprite.getInterpolatedV(renderMinZ * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinY * 16D);
			d6 = sprite.getInterpolatedV(renderMaxZ * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateNorth == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinZ * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxZ * 16D);
			d5 = sprite.getInterpolatedV(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(renderMinY * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMinX;
		double d12 = y + renderMinY;
		double d13 = y + renderMaxY;
		double d14 = z + renderMinZ;
		double d15 = z + renderMaxZ;
		
		if(renderFromInside)
		{
			d14 = z + renderMaxZ;
			d15 = z + renderMinZ;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		
		tessellator.getBuffer().pos(d11, d13, d15).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d14).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d12, d14).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d12, d15).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFaceXPos(double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		Tessellator tessellator = Tessellator.getInstance();
		
		double d3 = sprite.getInterpolatedU(renderMinZ * 16D);
		double d4 = sprite.getInterpolatedU(renderMaxZ * 16D);
		
		if(field_152631_f)
		{
			d4 = sprite.getInterpolatedU((1D - renderMinZ) * 16D);
			d3 = sprite.getInterpolatedU((1D - renderMaxZ) * 16D);
		}
		
		double d5 = sprite.getInterpolatedV(16D - renderMaxY * 16D);
		double d6 = sprite.getInterpolatedV(16D - renderMinY * 16D);
		
		if(flipTexture)
		{
			double d7 = d3;
			d3 = d4;
			d4 = d7;
		}
		
		if(renderMinZ < 0D || renderMaxZ > 1D)
		{
			d3 = sprite.getMinU();
			d4 = sprite.getMaxU();
		}
		
		if(renderMinY < 0D || renderMaxY > 1D)
		{
			d5 = sprite.getMinV();
			d6 = sprite.getMaxV();
		}
		
		double d7 = d4;
		double d8 = d3;
		double d9 = d5;
		double d10 = d6;
		
		if(uvRotateSouth == 2)
		{
			d3 = sprite.getInterpolatedU(renderMinY * 16D);
			d5 = sprite.getInterpolatedV(16D - renderMinZ * 16D);
			d4 = sprite.getInterpolatedU(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(16D - renderMaxZ * 16D);
			d9 = d5;
			d10 = d6;
			d7 = d3;
			d8 = d4;
			d5 = d6;
			d6 = d9;
		} else if(uvRotateSouth == 1)
		{
			d3 = sprite.getInterpolatedU(16D - renderMaxY * 16D);
			d5 = sprite.getInterpolatedV(renderMaxZ * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMinY * 16D);
			d6 = sprite.getInterpolatedV(renderMinZ * 16D);
			d7 = d4;
			d8 = d3;
			d3 = d4;
			d4 = d8;
			d9 = d6;
			d10 = d5;
		} else if(uvRotateSouth == 3)
		{
			d3 = sprite.getInterpolatedU(16D - renderMinZ * 16D);
			d4 = sprite.getInterpolatedU(16D - renderMaxZ * 16D);
			d5 = sprite.getInterpolatedV(renderMaxY * 16D);
			d6 = sprite.getInterpolatedV(renderMinY * 16D);
			d7 = d4;
			d8 = d3;
			d9 = d5;
			d10 = d6;
		}
		
		double d11 = x + renderMaxX;
		double d12 = y + renderMinY;
		double d13 = y + renderMaxY;
		double d14 = z + renderMinZ;
		double d15 = z + renderMaxZ;
		
		if(renderFromInside)
		{
			d14 = z + renderMaxZ;
			d15 = z + renderMinZ;
		}
		
		int i = bright;
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		tessellator.getBuffer().pos(d11, d12, d15).tex(d8, d10).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d12, d14).tex(d4, d6).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d14).tex(d7, d9).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
		tessellator.getBuffer().pos(d11, d13, d15).tex(d3, d5).lightmap(j, k).color(red, green, blue, renderAlpha).endVertex();
	}
	
	public void renderFace(EnumFacing face, double x, double y, double z, TextureAtlasSprite sprite, float red, float green, float blue, int bright)
	{
		if(face == EnumFacing.DOWN)
			renderFaceYNeg(x, y, z, sprite, red, green, blue, bright);
		if(face == EnumFacing.UP)
			renderFaceYPos(x, y, z, sprite, red, green, blue, bright);
		if(face == EnumFacing.NORTH)
			renderFaceZNeg(x, y, z, sprite, red, green, blue, bright);
		if(face == EnumFacing.SOUTH)
			renderFaceZPos(x, y, z, sprite, red, green, blue, bright);
		if(face == EnumFacing.WEST)
			renderFaceXNeg(x, y, z, sprite, red, green, blue, bright);
		if(face == EnumFacing.EAST)
			renderFaceXPos(x, y, z, sprite, red, green, blue, bright);
	}
	
	private static final Map<String, RenderBlocks> INSTANCES = new HashMap<>();
	
	@Nonnull
	public static RenderBlocks getInstance()
	{
		if(instance == null)
			instance = new RenderBlocks();
		return instance;
	}
	
	@Nonnull
	public static RenderBlocks forMod(String modid)
	{
		RenderBlocks i = INSTANCES.get(modid);
		if(i == null)
			INSTANCES.put(modid, i = new RenderBlocks());
		return i;
	}
}
