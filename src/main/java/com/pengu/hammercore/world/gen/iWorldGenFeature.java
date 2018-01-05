package com.pengu.hammercore.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public interface iWorldGenFeature
{
	int getMaxChances(World world, ChunkPos chunk, Random rand);
	
	int getMinY(World world, BlockPos pos, Random rand);
	
	int getMaxY(World world, BlockPos pos, Random rand);
	
	void generate(World world, BlockPos pos, Random rand);
}