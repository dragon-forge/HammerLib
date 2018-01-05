package com.pengu.hammercore.var;

import net.minecraft.nbt.NBTTagCompound;

public interface iVariable<T>
{
	T get();
	
	void set(T t);
	
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
	
	boolean hasChanged();
	
	void setNotChanged();
	
	String getId();
}