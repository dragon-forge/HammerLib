package org.zeith.hammerlib.net.properties;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.fluids.FluidStack;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyFluidStack
		extends PropertyBase<FluidStack>
{
	public PropertyFluidStack(DirectStorage<FluidStack> value)
	{
		super(FluidStack.class, value);
	}
	
	public PropertyFluidStack()
	{
		super(FluidStack.class, DirectStorage.allocate(FluidStack.EMPTY));
	}
	
	@Override
	protected boolean differ(FluidStack a, FluidStack b)
	{
		return !FluidStack.isSameFluidSameComponents(a, b);
	}
	
	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		FluidStack value = this.value.get();
		FluidStack.STREAM_CODEC.encode(buf, value);
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		value.set(FluidStack.STREAM_CODEC.decode(buf));
	}
}