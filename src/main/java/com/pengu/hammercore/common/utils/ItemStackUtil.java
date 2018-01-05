package com.pengu.hammercore.common.utils;

import java.util.Arrays;
import java.util.List;

import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Contains some utilities to make life with {@link ItemStack}s easier
 */
public class ItemStackUtil
{
	public static boolean tagsEqual(NBTTagCompound a, NBTTagCompound b)
	{
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
	
	public static boolean itemsEqual(ItemStack a, ItemStack b)
	{
		if(("" + a).equals("" + b))
			return true;
		if(InterItemStack.isStackNull(a) || InterItemStack.isStackNull(b))
			return false;
		return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && tagsEqual(a.getTagCompound(), b.getTagCompound());
	}
	
	public static boolean contains(ItemStack[] stacks, ItemStack stack)
	{
		stacks = Arrays.copyOf(stacks, stacks.length);
		stack = stack.copy();
		for(ItemStack s : stacks)
			if(itemsEqual(stack, s))
				return true;
		return false;
	}
	
	public static final class ItemDropData
	{
		public static final ItemDropData QUARRY_DROP_DATA = new ItemDropData(2, 12000);
		
		public ItemDropData(int pickupDelayMode, int despawnTime)
		{
			this.pickupDelayMode = pickupDelayMode;
			this.despawnTime = despawnTime;
		}
		
		/** 0 - never, 1 - normal, 2 - instant */
		public int pickupDelayMode = 1;
		public int despawnTime = 6000;
	}
	
	public static void splitOrDropItemAround(ItemStack stack, World w, BlockPos from, ItemDropData data, EnumFacing... blackist)
	{
		stack = splitItemAround(stack, w, from, blackist);
		if(shouldReturn(stack))
			return;
		EntityItem i = new EntityItem(w, from.getX() + .5, from.getY() + 1, from.getZ() + .5, stack);
		i.setVelocity(0, 0, 0);
		i.ticksExisted = -data.despawnTime;
		if(data.pickupDelayMode == 0)
			i.setInfinitePickupDelay();
		else if(data.pickupDelayMode == 2)
			i.setNoPickupDelay();
		w.spawnEntity(i);
	}
	
	public static ItemStack splitItemAround(ItemStack stack, World w, BlockPos from, EnumFacing... blackist)
	{
		if(w.isRemote)
			return stack;
		List<EnumFacing> b = Arrays.asList(blackist);
		for(EnumFacing f : EnumFacing.VALUES)
		{
			if(b.contains(f))
				continue;
			TileEntity entity = w.getTileEntity(from.offset(f));
			if(entity instanceof IInventory)
			{
				if(entity instanceof ISidedInventory)
				{
					ISidedInventory i = (ISidedInventory) entity;
					stack = insertSided(i, stack, f.getOpposite());
				} else
				{
					IInventory i = (IInventory) entity;
					stack = insert(i, stack);
				}
			}
			
			if(shouldReturn(stack))
				return null;
		}
		return stack;
	}
	
	public static ItemStack insert(IInventory to, ItemStack what)
	{
		for(int i = 0; i < to.getSizeInventory(); ++i)
		{
			if(!to.isItemValidForSlot(i, what))
				continue;
			ItemStack in = to.getStackInSlot(i);
			if(in == null)
			{
				to.setInventorySlotContents(i, what);
				return null;
			} else if(itemsEqual(what, in) && InterItemStack.getStackSize(in) < in.getMaxStackSize())
			{
				int howM = InterItemStack.getStackSize(what);
				while(howM + InterItemStack.getStackSize(in) > in.getMaxStackSize())
					howM--;
				InterItemStack.setStackSize(what, InterItemStack.getStackSize(what) - howM);
				InterItemStack.setStackSize(in, InterItemStack.getStackSize(in) + howM);
			}
			if(shouldReturn(what))
				return null;
		}
		return what;
	}
	
	public static ItemStack insertSided(ISidedInventory to, ItemStack what, EnumFacing from)
	{
		for(int i = 0; i < to.getSizeInventory(); ++i)
		{
			if(!to.canInsertItem(i, what, from) && !to.isItemValidForSlot(i, what))
				continue;
			ItemStack in = to.getStackInSlot(i);
			if(in == null)
			{
				to.setInventorySlotContents(i, what);
				return null;
			} else if(itemsEqual(what, in) && InterItemStack.getStackSize(in) < in.getMaxStackSize())
			{
				int howM = InterItemStack.getStackSize(what);
				while(howM + InterItemStack.getStackSize(in) > in.getMaxStackSize())
					howM--;
				InterItemStack.setStackSize(what, InterItemStack.getStackSize(what) - howM);
				InterItemStack.setStackSize(in, InterItemStack.getStackSize(in) + howM);
			}
			if(shouldReturn(what))
				return null;
		}
		return what;
	}
	
	public static boolean shouldReturn(ItemStack stack)
	{
		return stack == null || InterItemStack.getStackSize(stack) <= 0;
	}
}