package com.pengu.hammercore.asm;

import java.util.Random;

import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SnowfallHooks
{
	private static void meltSnow(World world, BlockPos pos, IBlockState state, Random rand)
	{
		// if(!HammerCoreConfigs.snowfall_enabled)
		// {
		// if(world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 ||
		// (world.getBiome(pos).getFloatTemperature(pos) >= .15F &&
		// HammerCoreConfigs.snowfall_meltSnow))
		// world.setBlockToAir(pos);
		// return;
		// }
		//
		// if(world.isRainingAt(pos) && rand.nextBoolean())
		// canSnowAtBody(world, pos, true);
		//
		// int meta = state.getBlock().getMetaFromState(state);
		// int k1 = meta & 0x7;
		//
		// if(world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 ||
		// (world.getBiome(pos).getFloatTemperature(pos) >= .15F &&
		// HammerCoreConfigs.snowfall_meltSnow))
		// {
		// while(world.getBlockState(pos = pos.up()).getBlock() ==
		// Blocks.SNOW_LAYER)
		// ;
		// pos = pos.down();
		//
		// meta = Blocks.SNOW_LAYER.getMetaFromState(world.getBlockState(pos)) &
		// 0x7;
		// try
		// {
		// if(meta < 1)
		// world.setBlockToAir(pos);
		// else
		// world.setBlockState(pos, state.getBlock().getStateFromMeta(meta -
		// 1));
		// } catch(Throwable err)
		// {
		// err.printStackTrace();
		// }
		// }
	}
	
	public static void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		// meltSnow(world, pos, state, rand);
		//
		// if(world.getBlockState(pos).getBlock() != Blocks.SNOW_LAYER ||
		// !HammerCoreConfigs.snowfall_balanceSnow)
		// return;
		//
		// /** The code below allows snow to get balanced */
		//
		// WorldLocation l = new WorldLocation(world, pos);
		// gl: for(EnumFacing f : EnumFacing.VALUES)
		// {
		// if(f.getAxis() == Axis.Y)
		// continue;
		//
		// boolean down = false;
		// WorldLocation al = new WorldLocation(world, pos.offset(f));
		//
		// while(al.getPos().getY() > 0 && (al.getBlock() == Blocks.AIR ||
		// al.getBlock() == Blocks.SNOW_LAYER))
		// {
		// if(al.getBlock() == Blocks.SNOW_LAYER && l.getBlock() ==
		// Blocks.SNOW_LAYER)
		// {
		// int fm = al.getMeta();
		// int lm = l.getMeta();
		//
		// if(fm - lm > 1 && !down)
		// {
		// al.setMeta(fm - 1);
		// l.setMeta(lm + 1);
		//
		// List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new
		// AxisAlignedBB(l.getPos()));
		// for(Entity e : ents)
		// e.move(MoverType.PISTON, 0, .125, 0);
		//
		// break gl;
		// } else if(lm > 0 && down)
		// {
		// al.setMeta(fm + 1);
		// l.setMeta(lm - 1);
		//
		// List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new
		// AxisAlignedBB(al.getPos()));
		// for(Entity e : ents)
		// e.move(MoverType.PISTON, 0, .125, 0);
		//
		// MinecraftServer s =
		// FMLCommonHandler.instance().getMinecraftServerInstance();
		//
		// if(s != null && !world.isRemote)
		// {
		// WorldServer w = s.getWorld(world.provider.getDimension());
		// w.spawnParticle(EnumParticleTypes.FALLING_DUST, l.getPos().getX() +
		// f.getFrontOffsetX() + .5 - .45 * f.getFrontOffsetX(),
		// l.getPos().getY() + .125 * (l.getMeta() + 1), l.getPos().getZ() +
		// f.getFrontOffsetZ() + .5 - .45 * f.getFrontOffsetZ(), 5,
		// Math.abs(f.getFrontOffsetZ()) * .45, 0, Math.abs(f.getFrontOffsetX())
		// * .45, 0, new int[] { Block.getStateId(Blocks.SNOW.getDefaultState())
		// });
		// }
		//
		// break gl;
		// }
		//
		// break;
		// }
		//
		// if(l.getBlock() == Blocks.SNOW_LAYER && l.getMeta() > 0 &&
		// al.getBlock() == Blocks.AIR)
		// {
		// if(Blocks.SNOW_LAYER.canPlaceBlockAt(world, al.getPos()))
		// {
		// al.setBlock(Blocks.SNOW_LAYER);
		// al.setMeta(0);
		// l.setMeta(l.getMeta() - 1);
		// List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new
		// AxisAlignedBB(al.getPos()));
		// for(Entity e : ents)
		// e.move(MoverType.PISTON, 0, .125, 0);
		// break gl;
		// }
		// }
		//
		// al = al.offset(EnumFacing.DOWN);
		// down = true;
		// }
		// }
	}
	
	public static boolean canSnowAtBody(World world, BlockPos pos, boolean checkLight)
	{
		// WorldLocation loc = new WorldLocation(world, pos);
		// Biome biome = loc.getBiome();
		// float f = biome.getFloatTemperature(pos);
		//
		// if(!HammerCoreConfigs.snowfall_enabled)
		// {
		// if(pos.getY() >= 0 && pos.getY() < 256 &&
		// world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
		// {
		// IBlockState iblockstate = world.getBlockState(pos);
		// if(iblockstate.getBlock().isAir(iblockstate, world, pos) &&
		// Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
		// return true;
		// }
		//
		// return false;
		// }
		//
		// if(!HammerCoreConfigs.snowfall_enabled)
		// return false;
		//
		// if(loc.getBlock() == Blocks.AIR)
		// {
		// if(f >= 0.15F && !HammerCoreConfigs.snowfall_snowWorld)
		// return false;
		// else if(!checkLight)
		// return true;
		// else
		// {
		// if(pos.getY() >= 0 && pos.getY() < 256 &&
		// world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
		// {
		// IBlockState iblockstate = world.getBlockState(pos);
		// if(iblockstate.getBlock().isAir(iblockstate, world, pos) &&
		// Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
		// return true;
		// }
		// return false;
		// }
		// }
		//
		// try
		// {
		// if(checkLight && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10 &&
		// loc.getBlock() == Blocks.SNOW_LAYER && !world.isRemote)
		// {
		// int smallest = loc.getMeta();
		//
		// if(world.rand.nextInt(smallest + 2) == 0)
		// {
		// int m = loc.getMeta();
		// if(m < 7)
		// grow(loc, m);
		//
		// return true;
		// }
		//
		// return false;
		// }
		// } catch(Throwable err)
		// {
		// }
		
		return false;
	}
	
	private static int findSmallest(WorldLocation loc, int smallest)
	{
		if(loc.getBlock() == Blocks.SNOW_LAYER)
			return Math.min(loc.getMeta(), smallest);
		if(Blocks.SNOW_LAYER.canPlaceBlockAt(loc.getWorld(), loc.getPos()))
			return -1;
		return smallest;
	}
	
	private static void grow(WorldLocation loc, int layers)
	{
		if((loc.getBlock() == Blocks.SNOW_LAYER && loc.getMeta() == layers) || (layers == -1 && Blocks.SNOW_LAYER.canPlaceBlockAt(loc.getWorld(), loc.getPos())))
			loc.setMeta(layers + 1);
	}
}