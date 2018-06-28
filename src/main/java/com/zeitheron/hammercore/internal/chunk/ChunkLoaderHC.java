package com.zeitheron.hammercore.internal.chunk;

import java.util.ArrayList;

import com.zeitheron.hammercore.api.IProcess;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.IChunkLoader;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.LoadableChunk;

import net.minecraft.world.World;

public enum ChunkLoaderHC implements IChunkLoader, IProcess
{
	INSTANCE;
	
	private final ArrayList<LoadableChunk> chunks = new ArrayList<>();
	private final ArrayList<Long> chunkTimers = new ArrayList<>();
	
	public void registerChunkWithTimeout(LoadableChunk chunk, long timer)
	{
		chunks.add(chunk);
		chunkTimers.add(timer);
	}
	
	@Override
	public void update()
	{
		for(int i = 0; i < chunks.size(); ++i)
		{
			long ticksLeft = chunkTimers.get(i);
			
			if(ticksLeft != -1L && ticksLeft > 0L)
			{
				ticksLeft--;
				chunkTimers.set(i, ticksLeft);
			}
			
			if(ticksLeft == 0L)
			{
				chunks.remove(i);
				chunkTimers.remove(i);
			}
		}
	}
	
	@Override
	public boolean isAlive()
	{
		chunks.clear();
		chunkTimers.clear();
		return true;
	}
	
	@Override
	public boolean shouldChunkBeLoaded(World world, int x, int z)
	{
		return false;
	}
	
	@Override
	public ArrayList<LoadableChunk> getForceLoadedChunks()
	{
		return chunks;
	}
}