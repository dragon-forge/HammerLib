package com.zeitheron.hammercore.net.props;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyString extends NetPropertyAbstract<String>
{
	public NetPropertyString(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyString(IPropertyChangeHandler handler, String initialValue)
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