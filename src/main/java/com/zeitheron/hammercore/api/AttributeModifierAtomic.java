package com.zeitheron.hammercore.api;

import java.util.UUID;
import java.util.function.DoubleSupplier;

import net.minecraft.entity.ai.attributes.AttributeModifier;

/**
 * The attribute modifier that may be implemented at any point in time.
 */
public class AttributeModifierAtomic extends AttributeModifier
{
	public final DoubleSupplier amountGet;
	
	public AttributeModifierAtomic(UUID idIn, String nameIn, DoubleSupplier amountIn, int operationIn)
	{
		super(idIn, nameIn, amountIn.getAsDouble(), operationIn);
		amountGet = amountIn;
	}
	
	@Override
	public double getAmount()
	{
		return amountGet.getAsDouble();
	}
}