package org.zeith.hammerlib.api.crafting.impl;

import net.minecraftforge.fluids.FluidStack;
import org.zeith.hammerlib.api.crafting.ICraftingResult;

public class FluidStackResult
		implements ICraftingResult<FluidStack>
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