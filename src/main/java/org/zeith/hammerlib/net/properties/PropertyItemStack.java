package org.zeith.hammerlib.net.properties;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyItemStack
		implements IProperty<ItemStack>
{
	final DirectStorage<ItemStack> value;

	public PropertyItemStack(DirectStorage<ItemStack> value)
	{
		this.value = value;
	}

	public PropertyItemStack()
	{
		this(DirectStorage.allocate(ItemStack.EMPTY));
	}

	public static boolean itemsEqual(ItemStack a, ItemStack b)
	{
		return ItemStack.matches(a, b);
	}

	@Override
	public Class<ItemStack> getType()
	{
		return ItemStack.class;
	}

	@Override
	public ItemStack set(ItemStack value)
	{
		ItemStack pv = this.value.get();
		if(!itemsEqual(pv, value))
		{
			this.value.set(value);
			markChanged(true);
		}
		return pv;
	}

	boolean changed;

	@Override
	public void markChanged(boolean changed)
	{
		this.changed = changed;
		if(changed) notifyDispatcherOfChange();
	}

	@Override
	public boolean hasChanged()
	{
		return changed;
	}

	@Override
	public void write(PacketBuffer buf)
	{
		ItemStack value = this.value.get();
		buf.writeItemStack(value, false);
	}

	@Override
	public void read(PacketBuffer buf)
	{
		value.set(buf.readItem());
	}

	@Override
	public ItemStack get()
	{
		return value.get();
	}

	PropertyDispatcher dispatcher;

	@Override
	public PropertyDispatcher getDispatcher()
	{
		return dispatcher;
	}

	@Override
	public void setDispatcher(PropertyDispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}
}