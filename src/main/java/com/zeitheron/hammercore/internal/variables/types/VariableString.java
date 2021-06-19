package com.zeitheron.hammercore.internal.variables.types;

import com.zeitheron.hammercore.internal.variables.IVariable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class VariableString
		implements IVariable<String>
{
	final ResourceLocation id;
	String var, prevVar;

	public VariableString(ResourceLocation id)
	{
		this.id = id;
	}

	@Override
	public String get()
	{
		return var;
	}

	@Override
	public void set(String t)
	{
		prevVar = var;
		var = t;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Var", var);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		var = nbt.getString("Var");
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