package com.zeitheron.hammercore.internal.chunk;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.annotations.MCFBus;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.IChunkLoader;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.LoadableChunk;
import com.zeitheron.hammercore.world.WorldGenHelper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@MCFBus
public class ChunkloadAPI
{
	private static ChunkPredicate predicate;
	
	{
		addChunkLoader(ChunkLoaderHC.INSTANCE);
		addChunkLoader(WorldGenHelper.chunkLoader());
	}
	
	public static void addChunkLoader(IChunkLoader loader)
	{
		predicate = new ChunkPredicate(predicate, loader);
	}
	
	public List<Long> chunks = new ArrayList<>();
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent e)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
		for(int i = 0; i < chunks.size(); ++i)
		{
			BlockPos pos = BlockPos.fromLong(chunks.get(i));
			
			int dim = pos.getX();
			int x = pos.getY();
			int z = pos.getZ();
			
			WorldServer ws = server.getWorld(dim);
			ws.getChunkProvider().provideChunk(x, z);
			
			chunks.remove(i);
		}
		
		ArrayList<LoadableChunk> chunks = getForcedChunks();
		for(int i = 0; i < chunks.size(); ++i)
		{
			LoadableChunk c = chunks.get(i);
			WorldServer ws = server.getWorld(c.dim);
			ws.getChunkProvider().provideChunk(c.x, c.z);
		}
	}
	
	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload evt)
	{
		Chunk c = evt.getChunk();
		if(shouldBeLoaded(c.getWorld(), c.x, c.z))
		{
			long l = new BlockPos(c.getWorld().provider.getDimension(), c.x, c.z).toLong();
			if(!chunks.contains(l))
				chunks.add(l);
		}
	}
	
	public static boolean shouldBeLoaded(World world, int x, int z)
	{
		if(predicate != null)
			return predicate.isKeptLoaded(world, x, z);
		return false;
	}
	
	public static ArrayList<LoadableChunk> getForcedChunks()
	{
		if(predicate != null)
			return predicate.getAllForcedChunks();
		return new ArrayList<>();
	}
}