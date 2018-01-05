package com.pengu.hammercore.var.types;

import java.util.Objects;

import com.pengu.hammercore.var.iVariable;

import net.minecraft.nbt.NBTTagCompound;

public class VariableString implements iVariable<String>
{
	final String id;
	String var, prevVar;
	
	public VariableString(String id)
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
	public String getId()
	{
		return id;
	}
	
}