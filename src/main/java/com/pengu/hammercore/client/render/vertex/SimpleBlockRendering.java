package com.pengu.hammercore.client.render.vertex;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.GLRenderState;
import com.pengu.hammercore.client.render.vertex.SpriteTexture.BlockSpriteTexture;
import com.pengu.hammercore.client.texture.TextureAtlasSpriteFull;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.hammercore.vec.Vector3;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class SimpleBlockRendering
{
	public final RenderBlocks rb;
	public BlockSpriteTexture[] textures = new BlockSpriteTexture[6];
	public double[] bounds = new double[6];
	public boolean[] faces = new boolean[6];
	public int[] rgb = new int[6];
	public int[] bright = new int[6];
	
	public SimpleBlockRendering(RenderBlocks rb)
	{
		this.rb = rb;
	}
	
	public void setColor(EnumFacing side, int rgb)
	{
		this.rgb[side.ordinal()] = rgb;
	}
	
	public void setBrightness(int bright)
	{
		Arrays.fill(this.bright, bright);
	}
	
	public void setBrightness(EnumFacing side, int bright)
	{
		this.bright[side.ordinal()] = bright;
	}
	
	public void disableFace(EnumFacing side)
	{
		faces[side.ordinal()] = false;
	}
	
	public void enableFace(EnumFacing side)
	{
		faces[side.ordinal()] = true;
	}
	
	public void enableFaces()
	{
		Arrays.fill(faces, true);
	}
	
	public void disableFaces()
	{
		Arrays.fill(faces, false);
	}
	
	public void setSidedSprites(TextureAtlasSprite bottom, TextureAtlasSprite top, TextureAtlasSprite side)
	{
		setSpriteForSide(EnumFacing.DOWN, bottom);
		setSpriteForSide(EnumFacing.UP, top);
		setSpriteForSide(EnumFacing.EAST, side);
		setSpriteForSide(EnumFacing.WEST, side);
		setSpriteForSide(EnumFacing.SOUTH, side);
		setSpriteForSide(EnumFacing.NORTH, side);
	}
	
	public void setSprite(TextureAtlasSprite sprite)
	{
		for(EnumFacing f : EnumFacing.VALUES)
			setSpriteForSide(f, sprite);
	}
	
	public void setSpriteForSide(EnumFacing side, TextureAtlasSprite sprite)
	{
		BlockSpriteTexture t = textures[side.ordinal()];
		if(t == null)
			textures[side.ordinal()] = t = new BlockSpriteTexture();
		t.setSprite(sprite);
	}
	
	private TextureAtlasSprite getSpriteForSide(EnumFacing side)
	{
		BlockSpriteTexture t = textures[side.ordinal()];
		if(t == null)
			textures[side.ordinal()] = t = new BlockSpriteTexture();
		return t.getSprite();
	}
	
	public void setRenderBounds(Cuboid6 cube)
	{
		setRenderBounds(cube.min, cube.max);
	}
	
	public void setRenderBounds(Vector3 min, Vector3 max)
	{
		setRenderBounds(min.x, min.y, min.z, max.x, max.y, max.z);
	}
	
	public void setRenderBounds(Vec3d min, Vec3d max)
	{
		setRenderBounds(min.x, min.y, min.z, max.x, max.y, max.z);
	}
	
	public void setRenderBounds(AxisAlignedBB aabb)
	{
		setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}
	
	public void setRenderBounds(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		setRenderBounds(state.getBlock().getBoundingBox(state, world, pos));
	}
	
	public void setRenderBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		bounds[0] = minX;
		bounds[1] = minY;
		bounds[2] = minZ;
		bounds[3] = maxX;
		bounds[4] = maxY;
		bounds[5] = maxZ;
	}
	
	public void begin()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		
		GLRenderState blend = GLRenderState.BLEND;
		blend.captureState();
		blend.on();
		
		enableFaces();
		
		Arrays.fill(rgb, 0xFFFFFF);
		Arrays.fill(bright, 255);
		
		setSprite(TextureAtlasSpriteFull.sprite);
		setRenderBounds(Block.FULL_BLOCK_AABB);
		Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		rb.renderFromInside = false;
	}
	
	public void drawBlock(double x, double y, double z)
	{
		rb.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		
		for(EnumFacing f : EnumFacing.VALUES)
			if(faces[f.ordinal()])
			{
				int rgb = this.rgb[f.ordinal()];
				int bright = this.bright[f.ordinal()];
				
				float r = (rgb >> 16 & 0xFF) / 255F;
				float g = (rgb >> 8 & 0xFF) / 255F;
				float b = (rgb >> 0 & 0xFF) / 255F;
				
				rb.renderFace(f, x, y, z, getSpriteForSide(f), r, g, b, bright);
			}
		
		enableFaces();
	}
	
	public void end()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator.getInstance().draw();
		
		GLRenderState.BLEND.reset();
	}
}