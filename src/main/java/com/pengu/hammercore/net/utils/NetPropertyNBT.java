package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyNBT<T extends NBTBase> extends NetPropertyAbstract<T>
{
	public NetPropertyNBT(iPropertyChangeHandler handler, T initialValue)
	{
		super(handler, initialValue);
	}
	
	public NetPropertyNBT(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		try
		{
			value = (T) nbt.getTag("Val");
		} catch(ClassCastException cce)
		{
			// Ignore, we have NBT overlap, probably.
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setTag("Val", value);
		return nbt;
	}
}