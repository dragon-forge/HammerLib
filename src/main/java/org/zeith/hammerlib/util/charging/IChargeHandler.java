package org.zeith.hammerlib.util.charging;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.lang.annotation.*;
import java.util.concurrent.atomic.AtomicReference;

public interface IChargeHandler<T extends AbstractCharge>
{
	/**
	 * @return the id that this handler may be found with
	 */
	String getId();

	/**
	 * @param stack  the stack being checked
	 * @param charge the charge being checked
	 * @return true if the given stack may be charged, false otherwise.
	 */
	boolean canCharge(ItemStack stack, T charge);

	/**
	 * Charges the given stack, if not simulating, and returns the leftover
	 * charge that wasn't used.
	 *
	 * @param stack  the stack being charged.
	 * @param charge the charge being applied to stack.
	 * @param action the action to perform to the stack.
	 * @return leftover charge that may be calculated like so:
	 * <code>charge - consumed</code>
	 */
	T charge(AtomicReference<ItemStack> stack, T charge, ChargeAction action);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface ChargeHandler
	{
		Class<? extends AbstractCharge> value();
	}

	enum ChargeAction
	{
		EXECUTE(IFluidHandler.FluidAction.EXECUTE),
		SIMULATE(IFluidHandler.FluidAction.SIMULATE);

		final IFluidHandler.FluidAction fluidAction;

		ChargeAction(IFluidHandler.FluidAction fluidAction)
		{
			this.fluidAction = fluidAction;
		}

		public IFluidHandler.FluidAction asFluidAction()
		{
			return fluidAction;
		}

		public boolean execute()
		{
			return this == EXECUTE;
		}

		public boolean simulate()
		{
			return this == SIMULATE;
		}
	}
}