package com.zeitheron.hammercore.internal.chunk;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.world.World;

public class ChunkPredicate
{
	public final ChunkPredicate parent;
	public final IChunkLoader loader;
	public final ArrayList<LoadableChunk> chunks = new ArrayList<>();
	
	public ChunkPredicate(ChunkPredicate parent, IChunkLoader loader)
	{
		this.parent = parent;
		this.loader = loader;
	}
	
	public final boolean isKeptLoaded(World world, int x, int z)
	{
		if(loader.shouldChunkBeLoaded(world, x, z))
			return true;
		return parent == null ? false : parent.isKeptLoaded(world, x, z);
	}
	
	public final ArrayList<LoadableChunk> getAllForcedChunks()
	{
		chunks.clear();
		chunks.addAll(loader.getForceLoadedChunks());
		if(parent != null)
			chunks.addAll(parent.getAllForcedChunks());
		return chunks;
	}
	
	public static interface IChunkLoader
	{
		default boolean shouldChunkBeLoaded(World world, int x, int z)
		{
			return false;
		}
		
		ArrayList<LoadableChunk> getForceLoadedChunks();
	}
	
	public static final class LoadableChunk implements Serializable
	{
		public final int dim, x, z;
		
		public LoadableChunk(int dim, int x, int z)
		{
			this.dim = dim;
			this.x = x;
			this.z = z;
		}
		
		@Override
		public int hashCode()
		{
			return (x + z * 31) * 31 + dim;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof LoadableChunk))
				return false;
			LoadableChunk b = (LoadableChunk) obj;
			return b.dim == dim && b.x == x && b.z == z;
		}
	}
}