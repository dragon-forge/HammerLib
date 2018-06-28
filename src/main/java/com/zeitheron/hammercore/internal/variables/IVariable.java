package com.zeitheron.hammercore.internal.variables;

import net.minecraft.nbt.NBTTagCompound;

public interface IVariable<T>
{
	T get();
	
	void set(T t);
	
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
	
	boolean hasChanged();
	
	void setNotChanged();
	
	String getId();
}