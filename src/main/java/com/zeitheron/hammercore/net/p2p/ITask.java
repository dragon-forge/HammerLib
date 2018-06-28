package com.zeitheron.hammercore.net.p2p;

import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;

public interface ITask
{
	void writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
	
	void execute(PacketContext ctx);
}