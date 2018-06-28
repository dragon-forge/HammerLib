package com.zeitheron.hammercore.world.data;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IChunkData extends INBTSerializable<NBTTagCompound>
{
	NBTTagCompound getDataTag();
	
	List<String> getRetrogenList1();
	
	List<String> getRetrogenList2();
}