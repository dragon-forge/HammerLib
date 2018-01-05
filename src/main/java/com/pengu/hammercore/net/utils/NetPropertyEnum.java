package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyEnum<T extends Enum> extends NetPropertyAbstract<T>
{
	public NetPropertyEnum(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyEnum(iPropertyChangeHandler handler, T initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
		{
			nbt.setInteger("Val", value.ordinal());
			nbt.setString("Class", value.getClass().getName());
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Class") && nbt.hasKey("Val"))
		{
			try
			{
				Class<T> c = (Class<T>) Class.forName(nbt.getString("Class"));
				T[] values = (T[]) c.getMethod("values").invoke(null);
				value = values[nbt.getInteger("Val")];
			} catch(Throwable err)
			{
			}
		}
	}
}