package com.zeitheron.hammercore.world.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkDataProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound>
{
	public final ChunkData data = new ChunkData();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == PerChunkDataManager.CHUNK_DATA;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == PerChunkDataManager.CHUNK_DATA ? (T) data : null;
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		return data.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		data.deserializeNBT(nbt);
	}
}