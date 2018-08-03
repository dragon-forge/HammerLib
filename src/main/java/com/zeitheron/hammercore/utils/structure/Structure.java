package com.zeitheron.hammercore.utils.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zeitheron.hammercore.client.render.world.VirtualWorld;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Structure
{
	protected final Map<Long, IBlockState> stateMap = new LinkedHashMap<Long, IBlockState>();
	protected final Map<Long, NBTTagCompound> tileMap = new LinkedHashMap<Long, NBTTagCompound>();
	
	public void placeStateAt(BlockPos pos, IBlockState state)
	{
		stateMap.put(pos.toLong(), state);
	}
	
	public void placeTileNBTAt(BlockPos pos, NBTTagCompound nbt)
	{
		tileMap.put(pos.toLong(), nbt);
	}
	
	public IBlockState getStateAt(BlockPos pos)
	{
		return stateMap.get(pos.toLong());
	}
	
	public NBTTagCompound getTileNBTAt(BlockPos pos)
	{
		return tileMap.get(pos.toLong()) != null ? tileMap.get(pos.toLong()).copy() : tileMap.get(pos.toLong());
	}
	
	protected void build(World world, BlockPos at)
	{
		for(Long l : stateMap.keySet().toArray(new Long[0]))
		{
			if(l == null)
				continue;
			BlockPos pos = BlockPos.fromLong(l);
			BlockPos tpos = at.add(pos);
			if(!world.isBlockLoaded(tpos))
				continue;
			NBTTagCompound nbt = tileMap.get(l);
			IBlockState state = getStateAt(pos);
			world.setBlockState(tpos, state);
			if(!world.isBlockLoaded(tpos))
				world.getChunk(tpos);
			if(nbt != null)
			{
				nbt = nbt.copy();
				nbt.setInteger("x", tpos.getX());
				nbt.setInteger("y", tpos.getY());
				nbt.setInteger("z", tpos.getZ());
				TileEntity tile = TileEntity.create(world, nbt);
				world.setTileEntity(at.add(pos), tile);
			}
		}
	}
	
	public void build(VirtualWorld world, BlockPos at)
	{
		for(Long l : stateMap.keySet().toArray(new Long[0]))
		{
			if(l == null)
				continue;
			BlockPos pos = BlockPos.fromLong(l);
			BlockPos tpos = at.add(pos);
			IBlockState state = getStateAt(pos);
			world.setBlockState(tpos, state);
		}
	}
	
	protected NBTTagCompound serialize()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(Long l : stateMap.keySet().toArray(new Long[0]))
		{
			IBlockState state = stateMap.get(l);
			NBTTagCompound tileData = tileMap.get(l);
			NBTTagCompound nbt0 = new NBTTagCompound();
			nbt0.setLong("Position", l);
			nbt0.setString("Block", state.getBlock().getRegistryName().toString());
			nbt0.setInteger("Metadata", state.getBlock().getMetaFromState(state));
			if(tileData != null)
				nbt0.setTag("TileData", tileData);
			list.appendTag(nbt0);
		}
		nbt.setTag("Blocks", list);
		return nbt;
	}
	
	protected void deserialize(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Blocks", 10);
		
		for(int i = 0; i < list.tagCount(); ++i)
		{
			try
			{
				NBTTagCompound nbt0 = list.getCompoundTagAt(i);
				
				long posLong = nbt0.getLong("Position");
				BlockPos pos = BlockPos.fromLong(posLong);
				IBlockState state = Block.getBlockFromName(nbt0.getString("Block")).getStateFromMeta(nbt0.getInteger("Metadata"));
				NBTTagCompound tile = nbt0.getCompoundTag("TileData");
				
				placeStateAt(pos, state);
				placeTileNBTAt(pos, tile);
			} catch(Throwable err)
			{
			} // Should only happen when block is missing, or mod maker is silly
			  // with tile entity readFromNBT method.
		}
	}
}