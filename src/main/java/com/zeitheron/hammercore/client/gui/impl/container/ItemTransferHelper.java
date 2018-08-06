package com.zeitheron.hammercore.client.gui.impl.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A shift-click helper for {@link TransferableContainer}.
 */
public class ItemTransferHelper
{
	public final TransferableContainer container;
	private TwoTuple<Integer, Integer> inventory = new TwoTuple<>(0, 0);
	
	private Map<Integer, Predicate<Integer>> outHandlers = new HashMap<>();
	private Map<Integer, Predicate<ItemStack>> inHandlers = new HashMap<>();
	private List<Integer> toInventory = new ArrayList<>();
	
	public ItemTransferHelper(TransferableContainer container)
	{
		this.container = container;
	}
	
	/**
	 * Adds a transfer rule into slot.
	 * 
	 * @param targetSlot
	 *            the slot that items will be shift-clicked in.
	 * @param valid
	 *            if the shift-clicked items are valid for the slot.
	 */
	public ItemTransferHelper addInTransferRule(int targetSlot, Predicate<ItemStack> valid)
	{
		inHandlers.put(targetSlot, valid);
		return this;
	}
	
	/**
	 * Adds a transfer rule out of the slot.
	 * 
	 * @param targetSlot
	 *            the slot that items will be shift-clicked out.
	 * @param slots
	 *            if the item can go into slot.
	 */
	public ItemTransferHelper addOutTransferRule(int targetSlot, Predicate<Integer> slots)
	{
		outHandlers.put(targetSlot, slots);
		return this;
	}
	
	/**
	 * An internal method.
	 */
	public ItemTransferHelper setInventorySlots(int start, int end)
	{
		inventory = new TwoTuple<>(start, end);
		return this;
	}
	
	/**
	 * Marks this slot to shift-click items to invetory
	 * 
	 * @param slot
	 *            the slot that will be extractable using shift-click.
	 */
	public ItemTransferHelper toInventory(int slot)
	{
		if(!toInventory.contains(slot))
			toInventory.add(slot);
		return this;
	}
	
	/**
	 * An internal method.
	 */
	public boolean isInventorySlot(int slot)
	{
		return slot >= inventory.get1() && slot < inventory.get2();
	}
	
	/**
	 * An internal method.
	 */
	public ItemStack handleTransfer(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = container.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			Predicate<Integer> slots = outHandlers.get(index);
			
			if(slots != null)
				for(int i = 0; i < container.inventorySlots.size(); ++i)
				{
					if(i == index || !slots.test(i))
						continue;
					
					Predicate<ItemStack> stacks = inHandlers.get(i);
					if(stacks != null && stacks.test(itemstack))
						continue;
					
					if(!container.mergeItemStack(itemstack1, i, i + 1, true))
						return ItemStack.EMPTY;
					slot.onSlotChange(itemstack1, itemstack);
				}
			else if(toInventory.contains(index))
			{
				if(!container.mergeItemStack(itemstack1, inventory.get1(), inventory.get2(), false))
					return ItemStack.EMPTY;
				slot.onSlotChange(itemstack1, itemstack);
			} else if(index != 0)
			{
				boolean functioned = false;
				for(Integer i : inHandlers.keySet())
				{
					Predicate<ItemStack> stacks = inHandlers.get(i);
					if(!stacks.test(itemstack))
						continue;
					functioned = true;
					if(!container.mergeItemStack(itemstack1, i, i + 1, false))
						return ItemStack.EMPTY;
					break;
				}
				
				if(!functioned)
				{
					int start = inventory.get1();
					int end = inventory.get2();
					
					if(index >= start + 9 && index < end && !container.mergeItemStack(itemstack1, start, start + 9, false))
						return ItemStack.EMPTY;
					
					else if(index >= start && index < start + 9 && !container.mergeItemStack(itemstack1, start + 9, end, false))
						return ItemStack.EMPTY;
				}
			}
			
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			
			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
	
	/**
	 * A container with simplified shift-clicking algorithm.
	 */
	public static abstract class TransferableContainer<T> extends Container
	{
		public final ItemTransferHelper transfer = new ItemTransferHelper(this);
		public final T t;
		public final EntityPlayer player;
		
		/**
		 * Constructs a new container.
		 * 
		 * @param player
		 *            The player that opened the container.
		 * @param t
		 *            The object opened.
		 * @param invX
		 *            the start X position for inventory slots.
		 * @param invY
		 *            the start Y position for inventory slots.
		 */
		public TransferableContainer(EntityPlayer player, T t, int invX, int invY)
		{
			this.t = t;
			this.player = player;
			addCustomSlots();
			addInventorySlots(player, invX, invY);
			addTransfer();
		}
		
		/**
		 * Override to implement custom slots to inventory.
		 */
		protected void addCustomSlots()
		{
		}
		
		/**
		 * Override to add shift-click rules. See {@link #transfer}'s methods
		 * for more info.
		 */
		protected void addTransfer()
		{
		}
		
		/**
		 * An internal method.
		 */
		protected void addInventorySlots(EntityPlayer player, int x, int y)
		{
			int start = inventorySlots.size();
			
			for(int i = 0; i < 9; ++i)
				addSlotToContainer(new Slot(player.inventory, i, x + i * 18, 58 + y));
			
			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 9; ++j)
					addSlotToContainer(new Slot(player.inventory, 9 + j + i * 9, x + 18 * j, y + i * 18));
				
			transfer.setInventorySlots(start, inventorySlots.size());
		}
		
		/**
		 * An internal method.
		 */
		@Override
		public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
		{
			return transfer.handleTransfer(playerIn, index);
		}
		
		@Override
		public boolean canInteractWith(EntityPlayer playerIn)
		{
			return true;
		}
		
		/**
		 * Make sure we catch any exceptions to prevent crashes.
		 */
		@Override
		protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
		{
			try
			{
				return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
			} catch(Throwable er)
			{
				// Safe check
			}
			
			return false;
		}
	}
}