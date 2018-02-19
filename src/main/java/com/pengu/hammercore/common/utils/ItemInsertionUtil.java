package com.pengu.hammercore.common.utils;

import javax.annotation.Nullable;

import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Allows to insert items like vanilla hoppers do
 */
public class ItemInsertionUtil
{
	public static boolean insertVanilla(TileEntity exportFromTE, IInventory exportFrom, EnumFacing facing)
	{
		TileEntity tileEntity = exportFromTE.getWorld().getTileEntity(exportFromTE.getPos().offset(facing));
		if(tileEntity == null)
		{
			return false;
		} else if(!tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
		{
			return false;
		} else
		{
			for(EnumFacing f : EnumFacing.VALUES)
			{
				IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, f);
				
				for(int i = 0; i < exportFrom.getSizeInventory(); ++i)
				{
					ItemStack stackInSlot = exportFrom.getStackInSlot(i);
					if(stackInSlot != null)
					{
						ItemStack insert = stackInSlot.copy();
						InterItemStack.setStackSize(insert, 1);
						ItemStack newStack = ItemHandlerHelper.insertItem(handler, insert, true);
						if(newStack == null || InterItemStack.getStackSize(newStack) == 0)
						{
							ItemHandlerHelper.insertItem(handler, exportFrom.decrStackSize(i, 1), false);
							exportFromTE.markDirty();
							return true;
						}
					}
				}
			}
			
			return true;
		}
	}
	
	public static boolean transferItemsOutFinal(TileEntity exportFromTE, IInventory exportFrom, EnumFacing facing)
	{
		if(insertVanilla(exportFromTE, exportFrom, facing))
		{
			return true;
		} else
		{
			IInventory iinventory = exportFromTE.getWorld().getTileEntity(exportFromTE.getPos().offset(facing)) instanceof IInventory ? ((IInventory) exportFromTE.getWorld().getTileEntity(exportFromTE.getPos().offset(facing))) : null;
			if(iinventory == null)
				return false;
			else
			{
				if(isInventoryFull(iinventory, facing))
					return false;
				else
				{
					for(EnumFacing f : EnumFacing.VALUES)
					{
						for(int i = 0; i < exportFrom.getSizeInventory(); ++i)
						{
							if(exportFrom.getStackInSlot(i) != null)
							{
								ItemStack itemstack = exportFrom.getStackInSlot(i).copy();
								ItemStack itemstack1 = putStackInInventoryAllSlots(iinventory, exportFrom.decrStackSize(i, 1), f);
								if(itemstack1 == null || InterItemStack.getStackSize(itemstack1) == 0)
								{
									iinventory.markDirty();
									return true;
								}
								
								exportFrom.setInventorySlotContents(i, itemstack);
							}
						}
					}
					
					return false;
				}
			}
		}
	}
	
	public static boolean isEmpty(IInventory inv)
	{
		for(int i$ = 0; i$ < inv.getSizeInventory(); ++i$)
		{
			ItemStack itemstack = inv.getStackInSlot(i$);
			if(itemstack != null)
				return false;
		}
		
		return true;
	}
	
	public static boolean isFull(IInventory inv)
	{
		for(int i$ = 0; i$ < inv.getSizeInventory(); ++i$)
		{
			ItemStack itemstack = inv.getStackInSlot(i$);
			if(itemstack == null || InterItemStack.getStackSize(itemstack) != itemstack.getMaxStackSize())
				return false;
		}
		
		return true;
	}
	
	public static boolean isInventoryFull(IInventory inv, EnumFacing facing)
	{
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory i = (ISidedInventory) inv;
			int[] j = i.getSlotsForFace(facing);
			for(int itemstack = 0; itemstack < j.length; ++itemstack)
			{
				ItemStack itemstack1 = i.getStackInSlot(j[itemstack]);
				if(itemstack1 == null || InterItemStack.getStackSize(itemstack1) != itemstack1.getMaxStackSize())
					return false;
			}
		} else
		{
			int var7 = inv.getSizeInventory();
			for(int var8 = 0; var8 < var7; ++var8)
			{
				ItemStack var9 = inv.getStackInSlot(var8);
				if(var9 == null || InterItemStack.getStackSize(var9) != var9.getMaxStackSize())
					return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInventoryEmpty(IInventory inv, EnumFacing facing)
	{
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory j = (ISidedInventory) inv;
			int[] k = j.getSlotsForFace(facing);
			for(int i = 0; i < k.length; ++i)
				if(j.getStackInSlot(k[i]) != null)
					return false;
		} else
		{
			int var5 = inv.getSizeInventory();
			for(int var6 = 0; var6 < var5; ++var6)
				if(inv.getStackInSlot(var6) != null)
					return false;
		}
		
		return true;
	}
	
	public static ItemStack putStackInInventoryAllSlots(IInventory inv, ItemStack stack, @Nullable EnumFacing face)
	{
		if(inv instanceof ISidedInventory && face != null)
		{
			ISidedInventory var6 = (ISidedInventory) inv;
			int[] var7 = var6.getSlotsForFace(face);
			for(int k = 0; k < var7.length && stack != null && InterItemStack.getStackSize(stack) > 0; ++k)
				stack = insertStack(inv, stack, var7[k], face);
		} else
		{
			int i = inv.getSizeInventory();
			for(int j = 0; j < i && stack != null && InterItemStack.getStackSize(stack) > 0; ++j)
				stack = insertStack(inv, stack, j, face);
		}
		
		if(stack != null && InterItemStack.getStackSize(stack) == 0)
			stack = null;
		
		return stack;
	}
	
	public static ItemStack insertStack(IInventory inv, ItemStack stack, int slot, EnumFacing face)
	{
		ItemStack itemstack = inv.getStackInSlot(slot);
		if(canInsertItemInSlot(inv, stack, slot, face))
		{
			boolean flag = false;
			int tileentityhopper;
			if(itemstack == null)
			{
				tileentityhopper = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
				if(tileentityhopper >= InterItemStack.getStackSize(stack))
				{
					inv.setInventorySlotContents(slot, stack);
					stack = null;
				} else
					inv.setInventorySlotContents(slot, stack.splitStack(tileentityhopper));
				
				flag = true;
			} else if(canCombine(itemstack, stack))
			{
				tileentityhopper = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
				if(tileentityhopper > InterItemStack.getStackSize(itemstack))
				{
					int i = tileentityhopper - InterItemStack.getStackSize(itemstack);
					int j = Math.min(InterItemStack.getStackSize(stack), i);
					InterItemStack.setStackSize(stack, InterItemStack.getStackSize(stack) - j);
					InterItemStack.setStackSize(itemstack, InterItemStack.getStackSize(itemstack) + j);
					flag = j > 0;
				}
			}
			
			if(flag)
			{
				if(inv instanceof TileEntityHopper)
				{
					TileEntityHopper tileentityhopper1 = (TileEntityHopper) inv;
					if(tileentityhopper1.mayTransfer())
						tileentityhopper1.setTransferCooldown(8);
					inv.markDirty();
				}
				
				inv.markDirty();
			}
		}
		
		return stack;
	}
	
	public static boolean canCombine(ItemStack stack, ItemStack with)
	{
		return stack.getItem() != with.getItem() ? false : (stack.getMetadata() != with.getMetadata() ? false : (InterItemStack.getStackSize(stack) > stack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack, with)));
	}
	
	public static boolean canInsertItemInSlot(IInventory inv, ItemStack side, int slot, EnumFacing face)
	{
		return !inv.isItemValidForSlot(slot, side) ? false : !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, side, face);
	}
}