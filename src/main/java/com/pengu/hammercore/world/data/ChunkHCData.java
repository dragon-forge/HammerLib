package com.pengu.hammercore.world.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class ChunkHCData implements INBTSerializable<NBTTagCompound>
{
	public NBTTagCompound nbt = new NBTTagCompound();
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.copy();
	}
}