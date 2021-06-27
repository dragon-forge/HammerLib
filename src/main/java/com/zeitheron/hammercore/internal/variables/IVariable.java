package com.zeitheron.hammercore.internal.variables;

import com.zeitheron.hammercore.utils.base.EvtBus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

public interface IVariable<T>
{
	T get();

	void set(T t);

	NBTTagCompound writeToNBT(NBTTagCompound nbt);

	void readFromNBT(NBTTagCompound nbt);

	boolean hasChanged();

	void setNotChanged();

	ResourceLocation getId();

	/**
	 * Sanity check, prevent clients from updating server's network variable!
	 */
	default NetworkDirection direction()
	{
		return NetworkDirection.SERVER_TO_CLIENT;
	}

	default void notifyUpdate(Side side)
	{
		/* This code sends out the variable refresh event to all listeners */
		EvtBus.postSafe(MinecraftForge.EVENT_BUS, new VariableRefreshEvent(getClass(), this, side));
	}
}