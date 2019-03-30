package com.zeitheron.hammercore.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryUtils
{
	@Deprecated
	public static ItemStack cycleItemStack(Object input)
	{
		return ItemStackUtil.cycleItemStack(input);
	}
	
	public static int takeItemsMax(ItemStack stack, int maxTake, IInventory inv)
	{
		int remaining = maxTake;
		for(int i = 0; i < inv.getSizeInventory(); ++i)
			if(InventoryUtils.equals(stack, inv.getStackInSlot(i), false))
			{
				while(remaining > 0 && InterItemStack.getStackSize(inv.getStackInSlot(i)) > 0)
				{
					remaining--;
					ItemStack j = inv.getStackInSlot(i);
					InterItemStack.setStackSize(j, InterItemStack.getStackSize(j) - 1);
				}
				
				if(InterItemStack.getStackSize(inv.getStackInSlot(i)) < 1)
					inv.setInventorySlotContents(i, InterItemStack.NULL_STACK);
				if(remaining < 1)
					return maxTake;
			}
		return maxTake - remaining;
	}
	
	public static boolean isInRange(World world, BlockPos pos, Block block, int rad)
	{
		if(block == null)
			return true;
		for(int x = -rad; x <= rad; ++x)
			for(int y = -rad; y <= rad; ++y)
				for(int z = -rad; z <= rad; ++z)
					if(world.getBlockState(pos.add(x, y, z)).getBlock() == block)
						return true;
		return false;
	}
	
	public static boolean containsAll(EntityPlayer player, ItemStack... stacks)
	{
		for(ItemStack stack : stacks)
			if(getTotalItemCount(player, stack) < InterItemStack.getStackSize(stack))
				return false;
		return true;
	}
	
	public static int getTotalItemCount(EntityPlayer player, ItemStack stack)
	{
		if(stack == null)
			return 0;
		
		int total = 0;
		
		for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
		{
			ItemStack stack0 = player.inventory.getStackInSlot(i);
			if(stack0 != null && equals(stack, stack0, false))
				total += InterItemStack.getStackSize(stack0);
		}
		
		return total;
	}
	
	public static boolean equals(ItemStack a, ItemStack b, boolean ignoreNBT)
	{
		if(InterItemStack.isStackNull(a) && InterItemStack.isStackNull(b))
			return true;
		if(InterItemStack.isStackNull(a) || InterItemStack.isStackNull(b))
			return false;
		boolean nbt = false;
		if(a.getTagCompound() == null && b.getTagCompound() == null)
			nbt = true;
		else if(a.getTagCompound() == null || b.getTagCompound() == null)
			nbt = false;
		else
			nbt = a.getTagCompound().equals(b.getTagCompound());
		return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage() && (ignoreNBT || nbt);
	}
}