package org.zeith.hammerlib.util.mcf.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public record FluidIngredientStack(FluidIngredient fluid, int amount)
		implements Predicate<FluidStack>
{
	public static final FluidIngredientStack EMPTY = new FluidIngredientStack(FluidIngredient.EMPTY, 0);
	
	public static final Codec<FluidIngredientStack> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidIngredientStack::fluid),
					Codec.INT.fieldOf("amount").forGetter(FluidIngredientStack::amount)
			).apply(instance, FluidIngredientStack::new)
	);
	
	public FluidIngredientStack(FluidIngredient fluid, int amount)
	{
		this.fluid = fluid.resolve();
		this.amount = amount;
	}
	
	private FluidIngredientStack resolve()
	{
		return isEmpty() ? EMPTY : this;
	}
	
	public boolean isEmpty()
	{
		return this == EMPTY || amount <= 0 || fluid.isEmpty();
	}
	
	public boolean fluidMatches(FluidStack fluidStack)
	{
		return fluid.test(fluidStack);
	}
	
	public FluidStack[] getValues()
	{
		return fluid.getValues(amount);
	}
	
	@Override
	public boolean test(FluidStack fluidStack)
	{
		return fluidStack.getAmount() >= amount && fluidMatches(fluidStack);
	}
}