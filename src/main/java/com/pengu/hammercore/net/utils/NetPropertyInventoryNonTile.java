package com.pengu.hammercore.net.utils;

import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.common.inventory.iInventoryListener;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyInventoryNonTile extends NetPropertyAbstract<InventoryNonTile> implements iInventoryListener
{
	public NetPropertyInventoryNonTile(iPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyInventoryNonTile(iPropertyChangeHandler handler, InventoryNonTile initialValue)
	{
		super(handler, initialValue);
		if(value != null)
			value.listener = this;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = new InventoryNonTile(0);
		value.readFromNBT(nbt);
		value.listener = this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		value.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void set(InventoryNonTile val)
	{
		super.set(val);
		if(value != null)
			value.listener = this;
	}
	
	@Override
	public InventoryNonTile getInventory()
	{
		return value;
	}
	
	@Override
	public void slotChange(int slot, ItemStack stack)
	{
		handler.notifyOfChange(this);
		if(syncOnChange)
			handler.sendChangesToNearby();
	}
}