package com.pengu.hammercore.common.inventory;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.WorldUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * This is a part of Hammer Core InventoryDummy is used widely to make inventory
 * code much more simple
 * 
 * @author APengu
 */
public class InventoryDummy implements IInventory
{
	public iInventoryListener listener;
	public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	private final int[] allSlots;
	public int inventoryStackLimit = 64;
	public iSlotPredicate validSlots = (i, stack) -> true;
	public NBTTagCompound boundCompound = new NBTTagCompound();
	
	public InventoryDummy(int inventorySize, NBTTagCompound boundNBT)
	{
		inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		allSlots = new int[inventory.size()];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
		boundCompound = boundNBT;
	}
	
	public InventoryDummy(NBTTagCompound boundNBT, ItemStack... items)
	{
		inventory = NonNullList.withSize(items.length, ItemStack.EMPTY);
		for(int i = 0; i < items.length; ++i)
			inventory.set(i, items[i]);
		allSlots = new int[items.length];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
		boundCompound = boundNBT;
	}
	
	public InventoryDummy(int inventorySize)
	{
		inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		allSlots = new int[inventorySize];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
	}
	
	public InventoryDummy(ItemStack... items)
	{
		inventory = NonNullList.withSize(items.length, ItemStack.EMPTY);
		for(int i = 0; i < items.length; ++i)
			inventory.set(i, items[i]);
		allSlots = new int[items.length];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
	}
	
	public int[] getAllAvaliableSlots()
	{
		return allSlots;
	}
	
	@Override
	public String getName()
	{
		return "Dummy Inventory";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.size();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		try
		{
			return inventory.get(index);
		} catch(Throwable err)
		{
		}
		return InterItemStack.NULL_STACK;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		try
		{
			if(!inventory.get(slot).isEmpty())
			{
				ItemStack is;
				
				if(inventory.get(slot).getCount() <= count)
				{
					is = inventory.get(slot);
					inventory.set(slot, ItemStack.EMPTY);
					
					if(listener != null)
						listener.slotChange(count, inventory.get(slot));
					return is;
				} else
				{
					is = inventory.get(slot).splitStack(count);
					if(inventory.get(slot).getCount() == 0)
						inventory.set(slot, ItemStack.EMPTY);
					if(listener != null)
						listener.slotChange(count, inventory.get(slot));
					return is;
				}
			}
		} catch(Throwable err)
		{
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		try
		{
			inventory.set(index, stack);
			if(listener != null)
				listener.slotChange(index, stack);
			if(inventory.get(index).getCount() > Math.min(inventory.get(index).getMaxStackSize(), getInventoryStackLimit()))
				inventory.get(index).setCount(Math.min(inventory.get(index).getMaxStackSize(), getInventoryStackLimit()));
		} catch(Throwable err)
		{
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventoryStackLimit;
	}
	
	@Override
	public void markDirty()
	{
		writeToNBT();
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return validSlots.test(index, stack);
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
		
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inventory.clear();
		if(listener != null)
			for(int i = 0; i < inventory.size(); ++i)
				listener.slotChange(i, inventory.get(i));
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(nbt != null)
		{
			nbt.setInteger("InvSize", inventory.size());
			ItemStackHelper.saveAllItems(nbt, inventory);
		}
		return nbt;
	}
	
	public void writeToNBT()
	{
		writeToNBT(boundCompound);
	}
	
	public InventoryDummy readFromNBT(NBTTagCompound nbt)
	{
		if(nbt != null)
		{
			inventory = NonNullList.withSize(nbt.getInteger("InvSize"), ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(nbt, inventory);
		}
		return this;
	}
	
	public void readFromNBT()
	{
		readFromNBT(boundCompound);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		ItemStack s = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		if(listener != null)
			listener.slotChange(slot, null);
		return s;
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	public boolean isUsableByPlayer(EntityPlayer player, BlockPos from)
	{
		return player.getDistanceSq(from) <= 64D;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return false;
	}
	
	public void drop(World world, BlockPos pos)
	{
		drop(this, world, pos);
	}
	
	public static void drop(IInventory inv, World world, BlockPos pos)
	{
		if(inv == null || world == null || pos == null)
			return;
		
		if(!world.isRemote)
			for(int i = 0; i < inv.getSizeInventory(); ++i)
			{
				ItemStack s = inv.getStackInSlot(i);
				if(InterItemStack.isStackNull(s))
					continue;
				WorldUtil.spawnItemStack(world, pos, s);
			}
		
		inv.clear();
	}
}