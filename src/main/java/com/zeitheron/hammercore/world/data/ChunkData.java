package com.zeitheron.hammercore.world.data;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.utils.NBTUtils;

import net.minecraft.nbt.NBTTagCompound;

public class ChunkData implements IChunkData
{
	public NBTTagCompound nbt = new NBTTagCompound();
	
	public final List<String> rgl1 = new ArrayList<>();
	public final List<String> rgl2 = new ArrayList<>();
	
	@Override
	public NBTTagCompound getDataTag()
	{
		return nbt;
	}
	
	@Override
	public List<String> getRetrogenList1()
	{
		return rgl1;
	}
	
	@Override
	public List<String> getRetrogenList2()
	{
		return rgl2;
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setTag("NBT", nbt.copy());
		NBTUtils.writeStringListToNBT(data, "L1", rgl1);
		NBTUtils.writeStringListToNBT(data, "L2", rgl2);
		return data;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		rgl1.clear();
		rgl2.clear();
		this.nbt = nbt.getCompoundTag("NBT").copy();
		rgl1.addAll(NBTUtils.readStringListFromNBT(nbt, "L1"));
		rgl2.addAll(NBTUtils.readStringListFromNBT(nbt, "L2"));
	}
}