package org.zeith.hammerlib.util.mcf.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public record FluidIngredientStack(FluidIngredient fluid, int amount)
		implements Predicate<FluidStack>
{
	public static final Codec<FluidIngredientStack> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidIngredientStack::fluid),
					Codec.INT.fieldOf("amount").forGetter(FluidIngredientStack::amount)
			).apply(instance, FluidIngredientStack::new)
	);
	
	@Override
	public boolean test(FluidStack fluidStack)
	{
		return fluidStack.getAmount() >= amount && fluid.test(fluidStack);
	}
}