package com.zeitheron.hammercore.client.model.mc;

import com.zeitheron.hammercore.api.inconnect.IBlockConnectable;
import com.zeitheron.hammercore.utils.PositionedStateImplementation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BakedConnectModel
		implements IBakedModel
{
	public static final FaceBakery FACE_BAKERY = new FaceBakery();
	public final IBlockState state;

	public BakedConnectModel(IBlockState state)
	{
		this.state = state;
	}

	enum EnumConnectedTextureV1Part
	{
		BASE(new float[]{
				6,
				6,
				10,
				10
		}), //
		CORNER_RD(new float[]{
				2,
				2,
				6,
				6
		}), //
		CORNER_LD(new float[]{
				10,
				2,
				14,
				6
		}), //
		CORNER_RU(new float[]{
				2,
				10,
				6,
				14
		}), //
		CORNER_LU(new float[]{
				10,
				10,
				14,
				14
		}), //
		SIDE_D(new float[]{
				6,
				2,
				10,
				6
		}), //
		SIDE_L(new float[]{
				10,
				6,
				14,
				10
		}), //
		SIDE_U(new float[]{
				6,
				10,
				10,
				14
		}), //
		SIDE_R(new float[]{
				2,
				6,
				6,
				10
		});

		final float[] uvs;

		private EnumConnectedTextureV1Part(float[] uvs)
		{
			this.uvs = uvs;
		}

		public float[] getUV()
		{
			return uvs;
		}

		BlockFaceUV bfuv;

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
					TextureAtlasSprite sprite = ct.getSpriteForFace(world, pos, state, side, rand, 0);

					AxisAlignedBB aabb = ct.getBlockShape(world, pos, pstate);

					quads.add(FACE_BAKERY.makeBakedQuad( //
							new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
							new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
							new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.BASE.asFaceUV()), //
							sprite, side, ModelRotation.X0_Y0, null, false, true));

					aabb = aabb.grow(.001);

					if(side.getAxis() == Axis.X)
					{
						boolean south = false, north = false, up = false, down = false;

						EnumFacing northf = side == EnumFacing.EAST ? EnumFacing.NORTH : EnumFacing.SOUTH, southf = northf.getOpposite();

						if(south = world.getBlockState(pos.offset(southf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_L.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(north = world.getBlockState(pos.offset(northf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_R.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(up = world.getBlockState(pos.up()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_U.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(down = world.getBlockState(pos.down()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_D.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(south || up || world.getBlockState(pos.offset(southf).offset(EnumFacing.UP)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(north || up || world.getBlockState(pos.offset(northf).offset(EnumFacing.UP)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(down || north || world.getBlockState(pos.offset(EnumFacing.DOWN).offset(northf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RD.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(south || down || world.getBlockState(pos.offset(southf).offset(EnumFacing.DOWN)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LD.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));
					}

					if(side.getAxis() == Axis.Y)
					{
						boolean west = false, east = false, north = false, south = false;

						EnumFacing northf = side == EnumFacing.UP ? EnumFacing.NORTH : EnumFacing.SOUTH, southf = northf.getOpposite();

						if(west = world.getBlockState(pos.west()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_L.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(east = world.getBlockState(pos.east()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_R.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(north = world.getBlockState(pos.offset(northf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_U.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(south = world.getBlockState(pos.offset(southf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_D.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(west || north || world.getBlockState(pos.offset(EnumFacing.WEST).offset(northf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(east || north || world.getBlockState(pos.offset(EnumFacing.EAST).offset(northf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(south || east || world.getBlockState(pos.offset(southf).offset(EnumFacing.EAST)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RD.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(west || south || world.getBlockState(pos.offset(EnumFacing.WEST).offset(southf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LD.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));
					}

					if(side.getAxis() == Axis.Z)
					{
						boolean west = false, east = false, up = false, down = false;

						EnumFacing westf = side == EnumFacing.SOUTH ? EnumFacing.WEST : EnumFacing.EAST, eastf = westf.getOpposite();

						if(west = world.getBlockState(pos.offset(westf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_L.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(east = world.getBlockState(pos.offset(eastf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_R.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(up = world.getBlockState(pos.up()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_U.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(down = world.getBlockState(pos.down()).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.SIDE_D.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(west || up || world.getBlockState(pos.offset(westf).offset(EnumFacing.UP)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(east || up || world.getBlockState(pos.offset(eastf).offset(EnumFacing.UP)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RU.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(down || east || world.getBlockState(pos.offset(EnumFacing.DOWN).offset(eastf)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_RD.asFaceUV()), //
									sprite, side, ModelRotation.X0_Y0, null, false, true));

						if(west || down || world.getBlockState(pos.offset(westf).offset(EnumFacing.DOWN)).getBlock() != block)
							quads.add(FACE_BAKERY.makeBakedQuad( //
									new Vector3f((float) aabb.minX * 16F, (float) aabb.minY * 16F, (float) aabb.minZ * 16F), //
									new Vector3f((float) aabb.maxX * 16F, (float) aabb.maxY * 16F, (float) aabb.maxZ * 16F), //
									new BlockPartFace(null, 0, "#0", EnumConnectedTextureV1Part.CORNER_LD.asFaceUV()), //
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