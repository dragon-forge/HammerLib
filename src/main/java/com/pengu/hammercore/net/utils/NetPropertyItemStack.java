package com.pengu.hammercore.net.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyItemStack extends NetPropertyAbstract<ItemStack>
{
	public NetPropertyItemStack(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyItemStack(iPropertyChangeHandler handler, ItemStack initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		get().writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = new ItemStack(nbt);
	}
	
	@Override
	public ItemStack get()
	{
		ItemStack val = super.get();
		return val == null ? ItemStack.EMPTY : val;
	}
	
	@Override
	public void set(ItemStack val)
	{
		if(val == null)
			val = ItemStack.EMPTY;
		super.set(val);
	}
}