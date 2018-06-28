package com.zeitheron.hammercore.net.props;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyByteArray extends NetPropertyAbstract<byte[]>
{
	public NetPropertyByteArray(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyByteArray(IPropertyChangeHandler handler, byte[] initialValue)
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