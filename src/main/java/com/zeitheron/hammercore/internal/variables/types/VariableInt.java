package com.zeitheron.hammercore.internal.variables.types;

import com.zeitheron.hammercore.internal.variables.BaseVariable;
import com.zeitheron.hammercore.internal.variables.IVariable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

public class VariableInt
		extends BaseVariable<Integer>
{
	public VariableInt(ResourceLocation id)
	{
		super(id);
	}

	@Override
	protected boolean hasChanged(Integer old, Integer updated)
	{
		return !Objects.equals(old, updated);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		Integer val = storage.get();
		if(val != null) nbt.setInteger("Var", val);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Var", Constants.NBT.TAG_INT))
			storage.set(nbt.getInteger("Var"));
		else storage.set(null);
	}
}