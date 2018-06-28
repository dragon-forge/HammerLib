package com.zeitheron.hammercore.net.props;

import com.zeitheron.hammercore.utils.inventory.IInventoryListener;
import com.zeitheron.hammercore.utils.inventory.InventoryDummy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyInventoryDummy extends NetPropertyAbstract<InventoryDummy> implements IInventoryListener
{
	public NetPropertyInventoryDummy(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyInventoryDummy(IPropertyChangeHandler handler, InventoryDummy initialValue)
	{
		super(handler, initialValue);
		if(value != null)
			value.listener = this;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = new InventoryDummy(0);
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
	public void set(InventoryDummy val)
	{
		super.set(val);
		if(value != null)
			value.listener = this;
	}
	
	@Override
	public InventoryDummy getInventory()
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