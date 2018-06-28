package com.zeitheron.hammercore.net.props;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyBool extends NetPropertyAbstract<Boolean>
{
	public NetPropertyBool(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyBool(IPropertyChangeHandler handler, boolean initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("Val", value == Boolean.TRUE);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("Val") && nbt.hasKey("val"))
			value = nbt.getBoolean("val");
		else
			value = nbt.getBoolean("Val");
	}
	
	@Override
	@Nonnull
	public Boolean get()
	{
		return super.get() == Boolean.TRUE;
	}
	
	@Override
	public void set(Boolean val)
	{
		super.set(val == Boolean.TRUE);
	}
}