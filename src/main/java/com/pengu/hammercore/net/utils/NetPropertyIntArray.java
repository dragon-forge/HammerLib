package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyIntArray extends NetPropertyAbstract<int[]>
{
	public NetPropertyIntArray(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyIntArray(iPropertyChangeHandler handler, int[] initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getIntArray("Val");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setIntArray("Val", value);
		return nbt;
	}
}