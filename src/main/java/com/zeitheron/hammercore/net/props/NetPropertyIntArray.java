package com.zeitheron.hammercore.net.props;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyIntArray extends NetPropertyAbstract<int[]>
{
	public NetPropertyIntArray(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyIntArray(IPropertyChangeHandler handler, int[] initialValue)
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