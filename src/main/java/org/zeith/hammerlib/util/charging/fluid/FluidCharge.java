package org.zeith.hammerlib.util.charging.fluid;

import net.minecraftforge.fluids.FluidStack;
import org.zeith.hammerlib.util.charging.AbstractCharge;

public class FluidCharge
		extends AbstractCharge
{
	public FluidStack fluid;

	public FluidCharge(FluidStack fluid)
	{
		this.fluid = fluid;
	}

	public FluidCharge discharge(int amt)
	{
		FluidCharge fc = copy();
		if(fc.fluid != null)
			fc.fluid.shrink(amt);
		return fc;
	}

	@Override
	public boolean containsCharge()
	{
		return fluid != null && !fluid.isEmpty() && fluid.getAmount() > 0;
	}

	@Override
	public FluidCharge copy()
	{
		return new FluidCharge(fluid == null ? null : fluid.copy());
	}
}