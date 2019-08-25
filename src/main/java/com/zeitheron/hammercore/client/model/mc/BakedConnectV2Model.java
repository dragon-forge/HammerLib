package com.zeitheron.hammercore.client.model.mc;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.zeitheron.hammercore.api.inconnect.IBlockConnectable;
import com.zeitheron.hammercore.utils.PositionedStateImplementation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BakedConnectV2Model implements IBakedModel
{
	public static final FaceBakery FACE_BAKERY = new FaceBakery();
	public final IBlockState state;
	
	public BakedConnectV2Model(IBlockState state)
	{
		this.state = state;
	}
	
	enum EnumConnectedTextureV2Part
	{
		BASE(new float[] { 0, 0, 4, 4 }), //
		CORNER_RD(new float[] { 4, 8, 8, 12 }), //
		CORNER_LD(new float[] { 0, 8, 4, 12 }), //
		CORNER_RU(new float[] { 4, 4, 8, 8 }), //
		CORNER_LU(new float[] { 0, 4, 4, 8 }), //
		SIDE_U(new float[] { 8, 0, 12, 4 }), //
		SIDE_L(new float[] { 8, 4, 12, 8 }), //
		SIDE_D(new float[] { 8, 8, 12, 12 }), //
		SIDE_R(new float[] { 8, 12, 12, 16 }), //
		SIDE_UF(new float[] { 12, 0, 16, 4 }), //
		SIDE_LF(new float[] { 12, 4, 16, 8 }), //
		SIDE_DF(new float[] { 12, 8, 16, 12 }), //
		SIDE_RF(new float[] { 12, 12, 16, 16 });
		
		static
		{
			SIDE_U.full = SIDE_UF;
			SIDE_L.full = SIDE_LF;
			SIDE_D.full = SIDE_DF;
			SIDE_R.full = SIDE_RF;
		}
		
		final float[] uvs;
		
		private EnumConnectedTextureV2Part(float[] uvs)
		{
			this.uvs = uvs;
		}
		
		public float[] getUV()
		{
			return uvs;
		}
		
		EnumConnectedTextureV2Part full;
		BlockFaceUV bfuv;
		
		public BlockFaceUV asFaceUV(boolean f)
		{
			return (f && full != null ? full : this).asFaceUV();
		}
		
		public BlockFaceUV asFaceUV()
		{
			return bfuv != null ? bfuv : (bfuv = new BlockFaceUV(getUV(), 0));
		}
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		if(side != null && state instanceof PositionedStateImplementation)
		{
			PositionedStateImplementation pstate = (PositionedStateImplementation) state;
			IBlockAccess world = pstate.getWorld();
			BlockPos pos = pstate.getPos();
			Block block = state.getBlock();
			
			IBlockConnectable ct = block instanceof IBlockConnectable ? (IBlockConnectable) block : null;
			
			if(world != null && pos != null && ct != null)
			{
				try
				{
					TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ct.getTxMap().toString());
					
					AxisAlignedBB aabb = ct.getBlockShape(world, pos, pstate);
					
					quads.add(FACE_BAKERY.makeBakedQuad( //
					        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
					        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
					        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.BASE.asFaceUV()), //
					        sprite, side, ModelRotation.X0_Y0, null, false, true));
					
					aabb = aabb.grow(.001);
					
					if(side.getAxis() == Axis.X)
					{
						EnumFacing northf = side == EnumFacing.EAST ? EnumFacing.NORTH : EnumFacing.SOUTH, southf = northf.getOpposite();
						
						boolean south = world.getBlockState(pos.offset(southf)).getBlock() != block, //
						        north = world.getBlockState(pos.offset(northf)).getBlock() != block, //
						        up = world.getBlockState(pos.up()).getBlock() != block, //
						        down = world.getBlockState(pos.down()).getBlock() != block;
						
						if(south)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_L.asFaceUV(!up || !down)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(north)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_R.asFaceUV(!up || !down)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(up)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_U.asFaceUV(!south || !north)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(down)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_D.asFaceUV(!south || !north)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						boolean a = south;
						boolean b = up;
						boolean c = world.getBlockState(pos.offset(southf).offset(EnumFacing.UP)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = north;
						b = up;
						c = world.getBlockState(pos.offset(northf).offset(EnumFacing.UP)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = down;
						b = north;
						c = world.getBlockState(pos.offset(EnumFacing.DOWN).offset(northf)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = south;
						b = down;
						c = world.getBlockState(pos.offset(southf).offset(EnumFacing.DOWN)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
					}
					
					if(side.getAxis() == Axis.Y)
					{
						EnumFacing northf = side == EnumFacing.UP ? EnumFacing.NORTH : EnumFacing.SOUTH, southf = northf.getOpposite();
						
						boolean west = world.getBlockState(pos.west()).getBlock() != block, //
						        east = world.getBlockState(pos.east()).getBlock() != block, //
						        north = world.getBlockState(pos.offset(northf)).getBlock() != block, //
						        south = world.getBlockState(pos.offset(southf)).getBlock() != block;
						
						if(west)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_L.asFaceUV(!south || !north)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(east)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_R.asFaceUV(!south || !north)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(north)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_U.asFaceUV(!west || !east)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(south)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_D.asFaceUV(!west || !east)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						boolean a = west;
						boolean b = north;
						boolean c = world.getBlockState(pos.offset(EnumFacing.WEST).offset(northf)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = east;
						b = north;
						c = world.getBlockState(pos.offset(EnumFacing.EAST).offset(northf)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = south;
						b = east;
						c = world.getBlockState(pos.offset(southf).offset(EnumFacing.EAST)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = west;
						b = south;
						c = world.getBlockState(pos.offset(EnumFacing.WEST).offset(southf)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
					}
					
					if(side.getAxis() == Axis.Z)
					{
						EnumFacing westf = side == EnumFacing.SOUTH ? EnumFacing.WEST : EnumFacing.EAST, eastf = westf.getOpposite();
						
						boolean west = world.getBlockState(pos.offset(westf)).getBlock() != block, //
						        east = world.getBlockState(pos.offset(eastf)).getBlock() != block, //
						        up = world.getBlockState(pos.up()).getBlock() != block, //
						        down = world.getBlockState(pos.down()).getBlock() != block;
						
						if(west)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_L.asFaceUV(!up || !down)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(east)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_R.asFaceUV(!up || !down)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(up)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_U.asFaceUV(!west || !east)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						if(down)
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.SIDE_D.asFaceUV(!west || !east)), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						boolean a = west;
						boolean b = up;
						boolean c = world.getBlockState(pos.offset(westf).offset(EnumFacing.UP)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = east;
						b = up;
						c = world.getBlockState(pos.offset(eastf).offset(EnumFacing.UP)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RU.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = down;
						b = east;
						c = world.getBlockState(pos.offset(EnumFacing.DOWN).offset(eastf)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_RD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
						
						a = west;
						b = down;
						c = world.getBlockState(pos.offset(westf).offset(EnumFacing.DOWN)).getBlock() != block;
						
						if((a && b) || (a && !c) || (!a && !b && c) || (b && !c))
							quads.add(FACE_BAKERY.makeBakedQuad( //
							        new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							        new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							        new BlockPartFace(null, 0, "#0", EnumConnectedTextureV2Part.CORNER_LD.asFaceUV()), //
							        sprite, side, ModelRotation.X0_Y0, null, false, true));
					}
				} catch(Throwable err)
				{
					err.printStackTrace();
				}
			}
		}
		return quads;
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}
	
	@Override
	public boolean isGui3d()
	{
		return false;
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return true;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		String tx = "minecraft:misingno";
		if(state.getBlock() instanceof IBlockConnectable)
		{
			IBlockConnectable c = (IBlockConnectable) state.getBlock();
			TextureAtlasSprite tas = c.getParticleTexture(state);
			if(tas != null)
				return tas;
			tx = c.getTxMap().toString();
		}
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tx);
	}
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}