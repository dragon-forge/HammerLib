package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyByteArray extends NetPropertyAbstract<byte[]>
{
	public NetPropertyByteArray(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyByteArray(iPropertyChangeHandler handler, byte[] initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getByteArray("Val");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setByteArray("Val", value);
		return nbt;
	}
}