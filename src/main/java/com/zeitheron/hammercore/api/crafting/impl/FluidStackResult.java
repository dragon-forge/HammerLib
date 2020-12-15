package com.zeitheron.hammercore.api.crafting.impl;

import com.zeitheron.hammercore.api.crafting.ICraftingResult;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackResult implements ICraftingResult<FluidStack>
{
	protected FluidStack output;
	
	public FluidStackResult(FluidStack output)
	{
		this.output = output;
	}
	
	@Override
	public FluidStack getBaseOutput()
	{
		return output.copy();
	}
	
	@Override
	public Class<FluidStack> getType()
	{
		return FluidStack.class;
	}
}