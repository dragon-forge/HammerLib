package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class PropertyItemStack
		extends PropertyBase<ItemStack>
{
	public PropertyItemStack(DirectStorage<ItemStack> value)
	{
		super(ItemStack.class, value);
	}
	
	public PropertyItemStack()
	{
		super(ItemStack.class);
	}
	
	@Override
	protected boolean differ(ItemStack a, ItemStack b)
	{
		return !ItemStack.areItemStacksEqual(a, b);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeItemStack(this.value.get());
	}
	
	@Override
	public void read(PacketBuffer buf)
			throws IOException
	{
		value.set(buf.readItemStack());
	}
}