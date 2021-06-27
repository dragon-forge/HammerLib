package com.zeitheron.hammercore.internal.variables.types;

import com.zeitheron.hammercore.internal.variables.BaseVariable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class VariableCompoundNBT
		extends BaseVariable<NBTTagCompound>
{
	public VariableCompoundNBT(ResourceLocation id)
	{
		super(id);
	}

	@Override
	protected boolean hasChanged(NBTTagCompound old, NBTTagCompound updated)
	{
		return !Objects.equals(old, updated);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		return storage.get();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		storage.set(nbt);
	}
}
