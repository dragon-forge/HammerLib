package com.pengu.hammercore.world.gen;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface iWorldGenFeature<T extends iWorldGenFeature> extends IForgeRegistryEntry<T>
{
	int getMaxChances(World world, ChunkPos chunk, Random rand);
	
	int getMinY(World world, BlockPos pos, Random rand);
	
	int getMaxY(World world, BlockPos pos, Random rand);
	
	void generate(World world, BlockPos pos, Random rand);
	
	ResourceLocation getRegistryName();
}