package com.pengu.hammercore.world.gen;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Predicates;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class WorldGenFeatureOre extends IForgeRegistryEntry.Impl<WorldGenFeatureOre> implements iWorldGenFeature<WorldGenFeatureOre>
{
	public int minY = 0, maxY = 64, maxClusters = 6, maxCusterSize = 8;
	public boolean enableBiomeWhitelist = false, enableDimensionWhitelist = false;
	public final IBlockState oreState;
	public IBlockState sourceState = Blocks.STONE.getDefaultState();
	public final Set<Integer> dimensionWhitelist = new HashSet<>();
	public final Set<Biome> biomes = new HashSet<>();
	
	public WorldGenFeatureOre(IBlockState ore)
	{
		oreState = ore;
	}
	
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return maxClusters;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return minY;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return maxY;
	}
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		boolean dimMatches = !enableDimensionWhitelist;
		if(!dimMatches)
			dimMatches = dimensionWhitelist.contains(world.provider.getDimension());
		
		boolean biomeMatches = !enableBiomeWhitelist;
		if(!biomeMatches)
			biomeMatches = biomes.contains(world.getBiome(pos));
		
		if(biomeMatches && dimMatches)
			new WorldGenMinable(oreState, maxCusterSize, Predicates.equalTo(sourceState)).generate(world, rand, pos);
	}
}