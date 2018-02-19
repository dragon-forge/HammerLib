package com.pengu.hammercore.common.utils;

import java.util.List;

import com.pengu.hammercore.common.InterItemStack;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryUtils
{
	public static ItemStack cycleItemStack(Object input)
	{
		NonNullList<ItemStack> q;
		ItemStack it = ItemStack.EMPTY;
		
		if(input instanceof ItemStack)
		{
			it = (ItemStack) input;
			
			if(it.getMetadata() == OreDictionary.WILDCARD_VALUE && it.getItem().getHasSubtypes())
			{
				NonNullList<ItemStack> q2 = NonNullList.create();
				it.getItem().getSubItems(it.getItem().getCreativeTab(), q2);
				if(q2 != null && q2.size() > 0)
				{
					int md = (int) (System.currentTimeMillis() / 1000 % q2.size());
					ItemStack it2 = new ItemStack(it.getItem(), 1, md);
					it2.setTagCompound(it.getTagCompound());
					it = it2;
				}
			} else if(it.getMetadata() == OreDictionary.WILDCARD_VALUE && it.isItemStackDamageable())
			{
				int md = (int) (System.currentTimeMillis() / 10 % it.getMaxDamage());
				ItemStack it2 = new ItemStack(it.getItem(), 1, md);
				it2.setTagCompound(it.getTagCompound());
				it = it2;
			}
			
			if(it.getMetadata() != 0 && !it.getHasSubtypes() && !it.isItemStackDamageable())
				it.setItemDamage(0);
		} else if(input instanceof ItemStack[])
		{
			ItemStack[] q3 = (ItemStack[]) input;
			if(q3 != null && q3.length > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000L % q3.length);
				it = InventoryUtils.cycleItemStack(q3[idx]);
			}
		} else if(input instanceof Ingredient)
			it = InventoryUtils.cycleItemStack(((Ingredient) input).getMatchingStacks());
		else if(input instanceof List)
		{
			List q3 = (List) input;
			if(q3 != null && q3.size() > 0)
			{
				int idx = (int) (System.currentTimeMillis() / 1000L % q3.size());
				it = InventoryUtils.cycleItemStack(q3.get(idx));
			}
		} else if(input instanceof String && !(q = OreDictionary.getOres(input + "")).isEmpty())
		{
			int idx = (int) (System.currentTimeMillis() / 1000L % q.size());
			it = InventoryUtils.cycleItemStack(q.get(idx));
		}
		
		return it;
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