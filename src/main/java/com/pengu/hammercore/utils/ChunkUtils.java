package com.pengu.hammercore.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
	public static BlockPos getChunkPos(int chunkX, int chunkZ, int offX, int offY, int offZ)
	{
		return new BlockPos(chunkX * 16 + offX, offY, chunkZ * 16 + offZ);
	}
	
	public static BlockPos getChunkPos(Chunk c, BlockPos off)
	{
		return getChunkPos(c.x, c.z, off.getX(), off.getY(), off.getZ());
	}
}