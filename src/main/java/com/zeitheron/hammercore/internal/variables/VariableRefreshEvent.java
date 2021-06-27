package com.zeitheron.hammercore.internal.variables;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Subscribe to this event to receive updates for when a variable is updated through a network!
 */
public class VariableRefreshEvent<E>
		extends GenericEvent<IVariable<E>>
{
	private final IVariable<E> var;
	private final Side side;

	public VariableRefreshEvent(Class<IVariable<E>> type, IVariable<E> var, Side side)
	{
		super(type);
		this.var = var;
		this.side = side;
	}

	public ResourceLocation getVariableId()
	{
		return var.getId();
	}

	public IVariable<E> getVariable()
	{
		return var;
	}
}