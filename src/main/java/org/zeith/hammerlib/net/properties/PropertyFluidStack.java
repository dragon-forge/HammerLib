package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
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
		return !a.isFluidStackIdentical(b);
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		FluidStack value = this.value.get();
		buf.writeFluidStack(value);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		value.set(buf.readFluidStack());
	}
}