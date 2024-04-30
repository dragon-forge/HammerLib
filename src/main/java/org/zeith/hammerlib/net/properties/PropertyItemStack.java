package org.zeith.hammerlib.net.properties;

import net.minecraft.network.RegistryFriendlyByteBuf;
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
	public void write(RegistryFriendlyByteBuf buf)
	{
		ItemStack.STREAM_CODEC.encode(buf, this.value.get());
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(ItemStack.STREAM_CODEC.decode(buf));
	}
}