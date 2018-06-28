package com.zeitheron.hammercore.world;

import java.util.Random;

import com.zeitheron.hammercore.world.gen.WorldRetroGen;
import com.zeitheron.hammercore.world.gen.IWorldGenFeature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHammerCore implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		ChunkPos cp = new ChunkPos(chunkX, chunkZ);
		Chunk c = world.getChunkFromChunkCoords(chunkX, chunkZ);
		
		/**
		 * Handle default retro-gen stuff. This means that chunks that didn't
		 * have X mod will generate later, when player comes by
		 */
		WorldRetroGen.generateChunk(c);
		
		for(IWorldGenFeature feature : WorldGenRegistry.listFeatures())
		{
			for(int i = 0; i < random.nextInt(feature.getMaxChances(world, cp, random)); ++i)
			{
				int genX = random.nextInt(16) + chunkX * 16;
				int genZ = random.nextInt(16) + chunkZ * 16;
				
				BlockPos pos = new BlockPos(genX, 0, genZ);
				
				int minY = feature.getMinY(world, pos, random);
				int yDelta = feature.getMaxY(world, pos, random) - minY;
				
				pos = new BlockPos(genX, random.nextInt(yDelta) + minY, genZ);
				
				feature.generate(world, pos, random);
			}
		}
	}
}