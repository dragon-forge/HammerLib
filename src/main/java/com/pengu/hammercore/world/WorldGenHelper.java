package com.pengu.hammercore.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nullable;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.chunk.ChunkPredicate.IChunkLoader;
import com.pengu.hammercore.common.chunk.ChunkPredicate.LoadableChunk;
import com.pengu.hammercore.event.WorldEventsHC;
import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.hammercore.world.gen.WorldRetroGen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@MCFBus
public class WorldGenHelper
{
	public static final Map<Integer, List<Long>> CHUNKLOADERS = new IndexedMap<>();
	private static final ArrayList<LoadableChunk> LOADED_CHUNKS = new ArrayList<>();
	
	/** Some handy {@link IBlockState} checkers for {@link #generateFlower} */
	public static final Predicate<IBlockState> //
	GRASS_OR_DIRT_CHECKER = state -> state != null && (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND), //
	        NETHERRACK_CHECKER = state -> state != null && state.getBlock() == Blocks.NETHERRACK, //
	        END_STONE_CHECKER = state -> state != null && state.getBlock() == Blocks.END_STONE;
	
	public static void loadChunk(int world, BlockPos chunkloader)
	{
		List<Long> longs = WorldGenHelper.CHUNKLOADERS.get(world);
		if(longs == null)
			WorldGenHelper.CHUNKLOADERS.put(world, longs = new ArrayList<>());
		if(!longs.contains(chunkloader.toLong()))
			longs.add(chunkloader.toLong());
		reloadChunks();
	}
	
	public static IChunkLoader chunkLoader()
	{
		return () -> LOADED_CHUNKS;
	}
	
	public static void unloadChunk(int world, BlockPos chunkloader)
	{
		List<Long> longs = WorldGenHelper.CHUNKLOADERS.get(world);
		if(longs == null)
			WorldGenHelper.CHUNKLOADERS.put(world, longs = new ArrayList<>());
		if(longs.contains(chunkloader.toLong()))
			longs.remove(chunkloader.toLong());
		reloadChunks();
	}
	
	public static void reloadChunks()
	{
		LOADED_CHUNKS.clear();
		for(Integer i : CHUNKLOADERS.keySet())
			for(Long l : CHUNKLOADERS.get(i))
			{
				BlockPos pos = BlockPos.fromLong(l);
				LOADED_CHUNKS.add(new LoadableChunk(i, pos.getX() >> 4, pos.getZ() >> 4));
			}
	}
	
	/**
	 * Generates a flower. WARNING: This method obtains world's height so you
	 * don't have to specify y level
	 */
	public static void generateFlowerOnSameY(IBlockState flower, Random rand, World world, BlockPos basePos, int maxSpawnRad, int minFlowers, int maxFlowers, boolean oneChunkOnly, Predicate<IBlockState> soilChecker)
	{
		int count = minFlowers + rand.nextInt(maxFlowers - minFlowers);
		int fails = 0;
		
		while(count > 0)
		{
			boolean planted = false;
			
			int x = basePos.getX() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			int z = basePos.getZ() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			
			BlockPos pos = new BlockPos(x, basePos.getY() - 1, z);
			
			if(oneChunkOnly && (pos.getX() >> 4 != basePos.getX() >> 4 || pos.getZ() >> 4 != basePos.getZ() >> 4))
				planted = false;
			else if(soilChecker.test(world.getBlockState(pos)) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
			{
				setBlockState(world, pos.up(), flower, null);
				planted = true;
			}
			
			if(!planted)
				++fails;
			
			if(fails >= 10 || planted)
			{
				fails = 0;
				--count;
			}
		}
	}
	
	public static void generateFlower(IBlockState flower, Random rand, World world, BlockPos basePos, int maxSpawnRad, int minFlowers, int maxFlowers, boolean oneChunkOnly, Predicate<IBlockState> soilChecker)
	{
		int count = minFlowers + rand.nextInt(maxFlowers - minFlowers);
		int fails = 0;
		
		while(count > 0)
		{
			boolean planted = false;
			
			int x = basePos.getX() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			int z = basePos.getZ() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			
			BlockPos pos = new BlockPos(x, world.getHeight(x, z) - 1, z);
			
			if(oneChunkOnly && (pos.getX() >> 4 != basePos.getX() >> 4 || pos.getZ() >> 4 != basePos.getZ() >> 4))
				planted = false;
			else if(soilChecker.test(world.getBlockState(pos)) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
			{
				setBlockState(world, pos.up(), flower, null);
				planted = true;
			}
			
			if(!planted)
				++fails;
			
			if(fails >= 10 || planted)
			{
				fails = 0;
				--count;
			}
		}
	}
	
	public static File getBlockSaveFile(World world)
	{
		return new File(world.getSaveHandler().getWorldDirectory(), "world_data.hc");
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, @Nullable TileEntity tile)
	{
		boolean logCascade = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		
		world.setBlockState(pos, state);
		world.setTileEntity(pos, tile);
		
		ForgeModContainer.logCascadingWorldGeneration = logCascade;
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state)
	{
		setBlockState(world, pos, state, null);
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker, @Nullable TileEntity tile)
	{
		boolean logCascade = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		
		world.setBlockState(pos, state, marker);
		world.setTileEntity(pos, tile);
		
		ForgeModContainer.logCascadingWorldGeneration = logCascade;
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker)
	{
		setBlockState(world, pos, state, marker, null);
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		
		if(HammerCore.AUTHORS.contains(player.getGameProfile().getName()))
			player.capabilities.allowFlying = true;
		
		if(e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
			return;
		
		if(player != null && !player.world.isRemote && player.ticksExisted % 10 == 0)
			for(int x = -8; x < 8; ++x)
				for(int z = -8; z < 8; ++z)
					WorldRetroGen.generateChunk(player.world.getChunkFromBlockCoords(player.getPosition().add(x * 16, 0, z * 16)));
	}
	
	@SubscribeEvent
	public void worldSaveEvt(WorldEvent.Save evt)
	{
		threadSaveCustomData(evt.getWorld());
	}
	
	public static void threadSaveCustomData(World world)
	{
		new Thread(() ->
		{
			try
			{
				ObjectOutputStream o = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(getBlockSaveFile(world))));
				o.writeObject(CHUNKLOADERS);
				IndexedMap<String, Serializable> pars = new IndexedMap<String, Serializable>();
				MinecraftForge.EVENT_BUS.post(new WorldEventsHC.SaveData(world, pars));
				o.writeObject(pars);
				o.close();
			} catch(FileNotFoundException e)
			{
				// Safely ignore
			} catch(Throwable err)
			{
				HammerCore.LOG.warn("Failed to save HammerCore custom data!");
				err.printStackTrace();
			}
		}).start();
	}
	
	public static void threadLoadCustomData(World world)
	{
		new Thread(() ->
		{
			try
			{
				IndexedMap<String, Serializable> pars;
				try
				{
					ObjectInputStream i = new ObjectInputStream(new GZIPInputStream(new FileInputStream(getBlockSaveFile(world))));
					CHUNKLOADERS.putAll((Map) i.readObject());
					pars = (IndexedMap<String, Serializable>) i.readObject();
					i.close();
				} catch(Throwable err)
				{
					if(!(err instanceof FileNotFoundException))
					{
						HammerCore.LOG.warn("Failed to load HammerCore custom data!");
						err.printStackTrace();
					} else
						HammerCore.LOG.warn("HammerCore custom data file not found!");
					pars = new IndexedMap<String, Serializable>();
				}
				MinecraftForge.EVENT_BUS.post(new WorldEventsHC.LoadData(world, pars));
			} catch(Throwable err)
			{
				HammerCore.LOG.warn("Failed to load HammerCore custom data!");
				err.printStackTrace();
			}
		}).start();
	}
	
	@SubscribeEvent
	public void worldLoadEvt(WorldEvent.Load evt)
	{
		threadLoadCustomData(evt.getWorld());
	}
}