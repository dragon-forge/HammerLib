package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * If you want to make your own version, please make constructor with argument
 * {@link iPropertyChangeHandler}!
 */
public abstract class NetPropertyAbstract<T>
{
	protected final iPropertyChangeHandler handler;
	protected final int id;
	protected T value;
	
	/** Determines if this property should get synced when it is changed */
	public boolean syncOnChange = true;
	
	public NetPropertyAbstract(iPropertyChangeHandler handler)
	{
		this.handler = handler;
		this.id = handler.registerProperty(this);
	}
	
	public NetPropertyAbstract(iPropertyChangeHandler handler, T initialValue)
	{
		this.handler = handler;
		this.id = handler.registerProperty(this);
		value = initialValue;
	}
	
	public int getId()
	{
		return id;
	}
	
	public abstract NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public T get()
	{
		return value;
	}
	
	public void set(T val)
	{
		if(!equals(val, value))
		{
			value = val;
			handler.notifyOfChange(this);
			if(syncOnChange)
				handler.sendChangesToNearby();
		}
	}
	
	private static boolean equals(Object a, Object b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		if(a.equals(b) && b.equals(a))
			return true;
		return false;
	}
}