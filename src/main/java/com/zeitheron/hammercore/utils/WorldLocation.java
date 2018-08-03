package com.zeitheron.hammercore.utils;

import javax.annotation.Nullable;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketSetBiome;
import com.zeitheron.hammercore.tile.TileSyncable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class WorldLocation
{
	private World world;
	private BlockPos pos;
	
	public WorldLocation(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public BlockPos getPos()
	{
		return pos;
	}
	
	public AxisAlignedBB getAABB()
	{
		return new AxisAlignedBB(pos);
	}
	
	public TileEntity getTile()
	{
		return world.isBlockLoaded(pos) ? world.getTileEntity(pos) : null;
	}
	
	public <T extends TileEntity> T getTileOfType(Class<T> tile)
	{
		return WorldUtil.cast(getTile(), tile);
	}
	
	public <T> T getTileOfInterface(Class<T> tile)
	{
		if(!tile.isInterface())
			throw new UnsupportedOperationException(tile.getName() + " is not an interface!");
		return WorldUtil.cast(getTile(), tile);
	}
	
	public boolean isLoaded()
	{
		return world.isBlockLoaded(pos);
	}
	
	public void setTile(TileEntity tile)
	{
		if(isLoaded())
			world.setTileEntity(pos, tile);
	}
	
	public IBlockState getState()
	{
		return isLoaded() ? world.getBlockState(pos) : Blocks.AIR.getDefaultState();
	}
	
	public Block getBlock()
	{
		return getState().getBlock();
	}
	
	public int getMeta()
	{
		return getBlock().getMetaFromState(getState());
	}
	
	public void setState(IBlockState state)
	{
		if(isLoaded())
			world.setBlockState(pos, state);
	}
	
	public void setState(IBlockState state, int flags)
	{
		if(isLoaded())
			world.setBlockState(pos, state, flags);
	}
	
	public void setAir()
	{
		if(isLoaded())
			world.setBlockToAir(pos);
	}
	
	public boolean isAir()
	{
		if(isLoaded())
			return getBlock() == Blocks.AIR;
		return true;
	}
	
	public void destroyBlock(boolean dropBlock)
	{
		if(isLoaded())
			world.destroyBlock(pos, dropBlock);
	}
	
	public boolean canSeeSky()
	{
		return canSeeSky(false);
	}
	
	public boolean canSeeSky(boolean __default)
	{
		if(isLoaded())
			return world.canSeeSky(pos);
		return __default;
	}
	
	public int getLight()
	{
		if(isLoaded())
			return world.getLight(pos);
		return 0;
	}
	
	public int getLightFor(EnumSkyBlock type)
	{
		if(isLoaded())
			return world.getLightFor(type, pos);
		return 0;
	}
	
	public BlockPos getHeight()
	{
		return world.getHeight(pos);
	}
	
	public void setLight(EnumSkyBlock type, int lightValue)
	{
		world.setLightFor(type, pos, lightValue);
	}
	
	public Biome getBiome()
	{
		return isLoaded() ? world.getBiome(pos) : Biomes.PLAINS;
	}
	
	public void setBiome(Biome biome)
	{
		if(isLoaded())
		{
			int i = pos.getX() & 15;
			int j = pos.getZ() & 15;
			int id = j << 4 | i;
			Chunk c = world.getChunk(pos);
			byte[] blockBiomeArray = c.getBiomeArray();
			blockBiomeArray[id] = (byte) (Biome.getIdForBiome(biome) & 255);
			c.setBiomeArray(blockBiomeArray);
			
			// update on client side
			if(!world.isRemote)
				HCNet.INSTANCE.sendToAllAround(new PacketSetBiome(pos.getX(), pos.getZ(), id, blockBiomeArray[id]), getPointWithRad(296));
		}
	}
	
	public boolean resetBiome()
	{
		Biome[] bfg = world.getBiomeProvider().getBiomesForGeneration(null, pos.getX(), pos.getZ(), 1, 1);
		if(bfg != null && bfg[0] != null && world.getBiome(pos) != bfg[0])
		{
			setBiome(bfg[0]);
			return true;
		}
		return false;
	}
	
	public TargetPoint getPointWithRad(int radius)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, radius);
	}
	
	public void setMeta(int meta)
	{
		if(isLoaded())
			world.setBlockState(pos, getBlock().getStateFromMeta(meta));
	}
	
	public void setBlock(Block block)
	{
		if(isLoaded())
			world.setBlockState(pos, block.getStateFromMeta(getMeta()));
	}
	
	public WorldLocation offset(EnumFacing facing)
	{
		return offset(facing, 1);
	}
	
	public WorldLocation offset(EnumFacing facing, int steps)
	{
		return new WorldLocation(world, pos.offset(facing, steps));
	}
	
	public WorldLocation offset(int x, int y, int z)
	{
		return new WorldLocation(world, pos.add(x, y, z));
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		world = null;
		pos = null;
		super.finalize();
	}
	
	public Material getMaterial()
	{
		return getBlock().getMaterial(getState());
	}
	
	public SoundType getSoundType()
	{
		return getSoundType(null);
	}
	
	public SoundType getSoundType(Entity e)
	{
		return getBlock().getSoundType(getState(), getWorld(), getPos(), e);
	}
	
	public int getRedstone()
	{
		return isLoaded() ? world.getRedstonePowerFromNeighbors(pos) : 0;
	}
	
	public void markDirty()
	{
		markDirty(3);
	}
	
	public Chunk getChunk()
	{
		return world.getChunk(pos);
	}
	
	public void markDirty(int flags)
	{
		if(world.isRemote)
			return;
		world.markAndNotifyBlock(pos, world.getChunk(pos), getState(), getState(), flags);
		TileSyncable sync = getTileOfType(TileSyncable.class);
		if(sync != null)
			sync.sync();
	}
	
	public void playSound(String sound, float volume, float pitch, SoundCategory cat)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, pos, volume, pitch, cat);
	}
	
	@Nullable
	public EntityItem dropItem(ItemStack stack)
	{
		if(!world.isRemote)
		{
			EntityItem ei = new EntityItem(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack);
			world.spawnEntity(ei);
			return ei;
		}
		return null;
	}
	
	@Nullable
	public EntityItem dropItem(ItemStack stack, double xOffset, double yOffset, double zOffset, double velX, double velY, double velZ)
	{
		if(!world.isRemote)
		{
			EntityItem ei = new EntityItem(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, stack);
			ei.motionX = velX;
			ei.motionY = velY;
			ei.motionZ = velZ;
			world.spawnEntity(ei);
			return ei;
		}
		return null;
	}
}