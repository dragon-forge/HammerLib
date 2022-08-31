package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.zeith.hammerlib.util.java.DirectStorage;

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
		return !ItemStack.matches(a, b);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		ItemStack value = this.value.get();
		buf.writeItemStack(value, false);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readItem());
	}
}