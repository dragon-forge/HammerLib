package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyString extends NetPropertyAbstract<String>
{
	public NetPropertyString(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyString(iPropertyChangeHandler handler, String initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null && !value.isEmpty())
			nbt.setString("Val", value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("Val") && nbt.hasKey("val"))
			value = nbt.getString("val");
		else
			value = nbt.getString("Val");
	}
	
	@Override
	public String get()
	{
		String s = super.get();
		return s == null ? "" : s;
	}
}