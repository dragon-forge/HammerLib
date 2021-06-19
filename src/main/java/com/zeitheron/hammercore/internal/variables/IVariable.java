package com.zeitheron.hammercore.internal.variables;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface IVariable<T>
{
	T get();
	
	void set(T t);
	
	NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	void readFromNBT(NBTTagCompound nbt);
	
	boolean hasChanged();
	
	void setNotChanged();

	ResourceLocation getId();
}