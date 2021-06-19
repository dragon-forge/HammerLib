package com.zeitheron.hammercore.internal.variables.types;

import com.zeitheron.hammercore.internal.variables.IVariable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class VariableInt
		implements IVariable<Integer>
{
	final ResourceLocation id;
	Integer var, prevVar;

	public VariableInt(ResourceLocation id)
	{
		this.id = id;
	}

	@Override
	public Integer get()
	{
		return var;
	}

	@Override
	public void set(Integer t)
	{
		prevVar = var;
		var = t;
	}

	public void setInt(int i)
	{
		set(i);
	}

	public int getInt()
	{
		return get() != null ? get() : 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Var", getInt());
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		setInt(nbt.getInteger("Var"));
	}

	@Override
	public boolean hasChanged()
	{
		return !Objects.equals(var, prevVar);
	}

	@Override
	public void setNotChanged()
	{
		prevVar = var;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}
}