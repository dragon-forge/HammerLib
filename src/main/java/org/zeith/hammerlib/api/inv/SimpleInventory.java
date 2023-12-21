package org.zeith.hammerlib.api.inv;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.*;

import java.util.Iterator;
import java.util.function.*;
import java.util.stream.Stream;

public class SimpleInventory
		implements IItemHandlerModifiable, Iterable<ItemStack>, INBTSerializable<ListTag>, Container
{
	public final NonNullList<ItemStack> items;

	public int stackSizeLimit = 64;
	public ToIntFunction<Integer> getSlotLimit = s -> stackSizeLimit;
	public BiPredicate<Integer, ItemStack> isStackValid = (i, s) -> true;

	public SimpleInventory(int slots)
	{
		this.items = NonNullList.withSize(slots, ItemStack.EMPTY);
	}

	@Override
	public int getSlots()
	{
		return items.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return items.get(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if(stack.isEmpty())
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getStackInSlot(slot);

		int m;
		if(!stackInSlot.isEmpty())
		{
			if(stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot)))
				return stack;

			if(!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
				return stack;

			if(!isItemValid(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

			if(stack.getCount() <= m)
			{
				if(!simulate)
				{
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					setStackInSlot(slot, copy);
				}

				return ItemStack.EMPTY;
			} else
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if(!simulate)
				{
					ItemStack copy = stack.split(m);
					copy.grow(stackInSlot.getCount());
					setStackInSlot(slot, copy);
					return stack;
				} else
				{
					stack.shrink(m);
					return stack;
				}
			}
		} else
		{
			if(!isItemValid(slot, stack))
				return stack;

			m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
			if(m < stack.getCount())
			{
				// copy the stack to not modify the original one
				stack = stack.copy();
				if(!simulate)
				{
					setStackInSlot(slot, stack.split(m));
					return stack;
				} else
				{
					stack.shrink(m);
					return stack;
				}
			} else
			{
				if(!simulate)
				{
					setStackInSlot(slot, stack);
				}
				return ItemStack.EMPTY;
			}
		}

	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if(amount == 0)
			return ItemStack.EMPTY;

		ItemStack stackInSlot = getStackInSlot(slot);

		if(stackInSlot.isEmpty())
			return ItemStack.EMPTY;

		if(simulate)
		{
			if(stackInSlot.getCount() < amount)
			{
				return stackInSlot.copy();
			} else
			{
				ItemStack copy = stackInSlot.copy();
				copy.setCount(amount);
				return copy;
			}
		} else
		{
			int m = Math.min(stackInSlot.getCount(), amount);
			ItemStack decrStackSize = decrStackSize(slot, m);
			return decrStackSize;
		}
	}

	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		return stack.split(amount);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return getSlotLimit.applyAsInt(slot);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return isStackValid.test(slot, stack);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack)
	{
		return isItemValid(slot, stack);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if(slot >= 0 && slot < items.size())
			items.set(slot, stack);
	}

	public void writeToNBT(CompoundTag nbt, String label)
	{
		nbt.put(label, writeToNBT(new ListTag()));
	}

	public void readFromNBT(CompoundTag nbt, String label)
	{
		readFromNBT(nbt.getList(label, Tag.TAG_COMPOUND));
	}

	public ListTag writeToNBT(ListTag nbt)
	{
		for(int i = 0; i < items.size(); ++i)
		{
			ItemStack stack = items.get(i);
			if(!stack.isEmpty())
			{
				CompoundTag tag = stack.save(new CompoundTag());
				tag.putInt("Slot", i);
				nbt.add(tag);
			}
		}
		return nbt;
	}

	public void readFromNBT(ListTag nbt)
	{
		for(int i = 0; i < nbt.size(); ++i)
		{
			CompoundTag tag = nbt.getCompound(i);
			ItemStack stack = ItemStack.of(tag);
			int slot = tag.getShort("Slot");
			if(slot >= 0 && slot < items.size())
				items.set(slot, stack);
		}
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		return items.iterator();
	}

	public Stream<ItemStack> stream()
	{
		return items.stream();
	}

	@Override
	public ListTag serializeNBT()
	{
		return writeToNBT(new ListTag());
	}

	@Override
	public void deserializeNBT(ListTag nbt)
	{
		readFromNBT(nbt);
	}

	@Override
	public int getContainerSize()
	{
		return getSlots();
	}

	@Override
	public boolean isEmpty()
	{
		for(ItemStack e : this)
			if(!e.isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return getStackInSlot(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int count)
	{
		ItemStack stack = items.get(slot);
		if(!stack.isEmpty()) return stack.split(count);
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		ItemStack itemstack = this.items.get(slot);
		if(itemstack.isEmpty())
		{
			return ItemStack.EMPTY;
		} else
		{
			this.items.set(slot, ItemStack.EMPTY);
			return itemstack;
		}
	}

	@Override
	public void setItem(int slot, ItemStack item)
	{
		this.items.set(slot, item);
		if(!item.isEmpty() && item.getCount() > this.getMaxStackSize())
			item.setCount(this.getMaxStackSize());
		this.setChanged();
	}

	@Override
	public void setChanged()
	{
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	@Override
	public void clearContent()
	{
		for(int i = 0; i < getSlots(); ++i)
			this.items.set(i, ItemStack.EMPTY);
	}
}