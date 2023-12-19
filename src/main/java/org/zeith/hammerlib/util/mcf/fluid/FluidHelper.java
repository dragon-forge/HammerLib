package org.zeith.hammerlib.util.mcf.fluid;

import net.neoforged.neoforge.fluids.FluidStack;

public class FluidHelper
{
	public static FluidStack limit(FluidStack fluid, int max)
	{
		if(fluid.isEmpty() || fluid.getAmount() <= max) return fluid;
		return withAmount(fluid, max);
	}
	
	public static FluidStack withAmount(FluidStack fluid, int amount)
	{
		if(fluid.isEmpty()) return FluidStack.EMPTY;
		var fs = fluid.copy();
		fs.setAmount(amount);
		return fs;
	}
}