package com.pengu.hammercore.common.chunk;

import java.util.ArrayList;

import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.common.chunk.ChunkPredicate.IChunkLoader;
import com.pengu.hammercore.common.chunk.ChunkPredicate.LoadableChunk;

import net.minecraft.world.World;

public enum ChunkLoaderHC implements IChunkLoader, iProcess
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