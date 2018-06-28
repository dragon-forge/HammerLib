package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundUtil
{
	public static void playSoundEffect(WorldLocation world, String sound, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world.getWorld(), sound, world.getPos(), volume, pitch, category);
	}
	
	public static void playSoundEffect(World world, String sound, BlockPos pos, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, pos, volume, pitch, category);
	}
	
	public static void playSoundEffect(World world, String sound, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, x, y, z, volume, pitch, category);
	}
	
	public static void playBlockStateBreak(World world, IBlockState type, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playBlockStateBreak(world, type, x, y, z, volume, pitch, category);
	}
}