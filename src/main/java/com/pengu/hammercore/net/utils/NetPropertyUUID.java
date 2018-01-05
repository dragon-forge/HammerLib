package com.pengu.hammercore.net.utils;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyUUID extends NetPropertyAbstract<UUID>
{
	public NetPropertyUUID(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyUUID(iPropertyChangeHandler handler, UUID initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setUniqueId("Val", value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("Val") && nbt.hasKey("val"))
			value = nbt.getUniqueId("val");
		else
			value = nbt.getUniqueId("Val");
	}
}