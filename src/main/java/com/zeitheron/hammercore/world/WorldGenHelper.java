package com.zeitheron.hammercore.world;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.MCFBus;
import com.zeitheron.hammercore.api.CustomMonsterAttributes;
import com.zeitheron.hammercore.api.GameRules;
import com.zeitheron.hammercore.event.WorldEventsHC;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.IChunkLoader;
import com.zeitheron.hammercore.internal.chunk.ChunkPredicate.LoadableChunk;
import com.zeitheron.hammercore.lib.zlib.utils.IndexedMap;
import com.zeitheron.hammercore.world.gen.WorldRetroGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandGameRule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Mod.EventBusSubscriber
public class WorldGenHelper
{
	public static final Map<Integer, List<Long>> CHUNKLOADERS = new IndexedMap<>();
	private static final ArrayList<LoadableChunk> LOADED_CHUNKS = new ArrayList<>();
	/**
	 * Some handy {@link IBlockState} checkers for {@link #generateFlower}
	 */
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
	 *
	 * @param flower       The {@link IBlockState} of the flower
	 * @param rand         The random
	 * @param world        The world
	 * @param basePos      The position
	 * @param maxSpawnRad  The maximal spread radius
	 * @param minFlowers   The minimal amount of flowers
	 * @param maxFlowers   The maximal amount of flowers
	 * @param oneChunkOnly Should flowers spawn in one chunk only
	 * @param soilChecker  What blocks should flowers be planted on
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

	public static final UUID FLIGHT_SPEED_UUID = UUID.fromString("08B6A944-A002-4715-BE52-E2DBAF61C4E9");
	public static final UUID MOVE_SPEED_UUID = UUID.fromString("08B6A944-A002-4715-BE52-E2DBAF61C4E9");

	@SubscribeEvent
	public static void chunkLoad(TickEvent.PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		if(e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
			return;
		if(player != null && !player.world.isRemote && player.ticksExisted % 10 == 0)
			for(int x = -4; x < 4; ++x)
				for(int z = -4; z < 4; ++z)
				{
					BlockPos pos = player.getPosition().add(x * 16, 0, z * 16);
					if(player.world.isBlockLoaded(pos))
						WorldRetroGen.generateChunk(player.world.getChunk(pos));
				}
	}

	@SubscribeEvent
	public static void entityInit(EntityEvent.EntityConstructing e)
	{
		if(e.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e.getEntity();
			p.getAttributeMap().registerAttribute(CustomMonsterAttributes.FLIGHT_SPEED);
			p.getAttributeMap().registerAttribute(CustomMonsterAttributes.WALK_SPEED);
		}
	}

	@SubscribeEvent
	public static void worldSaveEvt(WorldEvent.Save evt)
	{
		threadSaveCustomData(evt.getWorld());
	}

	@SubscribeEvent
	public static void worldLoadEvt(WorldEvent.Load evt)
	{
		threadLoadCustomData(evt.getWorld());
	}

	public static void threadSaveCustomData(World world)
	{
		new Thread(() ->
		{
			try
			{
				ObjectOutputStream o = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(getBlockSaveFile(world))));
				o.writeObject(CHUNKLOADERS);
				IndexedMap<String, Serializable> pars = new IndexedMap<>();
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
						HammerCore.LOG.warn("HammerCore custom data file not found! (this is not an issue!)");
					pars = new IndexedMap<>();
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
	public static void livingFall(LivingFallEvent e)
	{
		e.setDamageMultiplier(e.getDamageMultiplier() * GameRules.getEntry("hc_falldamagemult").getFloat(e.getEntity().world));
	}

	@SubscribeEvent
	public static void commandEvent(CommandEvent ce)
	{
		if(ce.getCommand() instanceof CommandGameRule)
		{
			String[] args = ce.getParameters();
			if(args.length == 1)
			{
				MinecraftServer mcs = ce.getSender().getServer();
				if(mcs != null)
				{
					String rule = args[0];
					if(GameRules.getEntry(rule).name.equals(rule))
						ce.getSender().sendMessage(new TextComponentTranslation(GameRules.getEntry(rule).i18nDesc));
				}
			}
		}
	}

	@SubscribeEvent
	public static void worldUpdate(WorldTickEvent e)
	{
		if(e.phase == Phase.START)
		{
			World w = e.world;
			if(!GameRules.getEntry("hc_rainfall").getBool(w))
			{
				WorldInfo info = w.getWorldInfo();
				info.setCleanWeatherTime(1_000_000);
				info.setRainTime(0);
				info.setThunderTime(0);
				info.setRaining(false);
				info.setThundering(false);
			}
		}
	}
}