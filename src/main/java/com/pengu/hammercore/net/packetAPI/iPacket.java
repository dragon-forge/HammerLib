package com.pengu.hammercore.net.packetAPI;

import net.minecraft.nbt.NBTTagCompound;

public interface iPacket
{
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}