package org.zeith.hammerlib.api.fluid;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IExtendedFluidType
{
	IExtendedFluidType DUMMY = new IExtendedFluidType() {};
	
	@NotNull
	static IExtendedFluidType of(FluidStack stack)
	{
		return stack != null && stack.getFluid().getFluidType() instanceof IExtendedFluidType e ? e : DUMMY;
	}
	
	default Optional<TooltipComponent> getTooltipImage(FluidStack stack, boolean showCapacity, int capacity, boolean advancedItemTooltips)
	{
		return Optional.empty();
	}
}