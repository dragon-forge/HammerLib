package com.zeitheron.hammercore.internal.variables.types;

import com.zeitheron.hammercore.internal.variables.BaseVariable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

public class VariableString
		extends BaseVariable<String>
{
	public VariableString(ResourceLocation id)
	{
		super(id);
	}

	@Override
	protected boolean hasChanged(String old, String updated)
	{
		return !Objects.equals(old, updated);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		String val = storage.get();
		if(val != null) nbt.setString("Var", val);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Var", Constants.NBT.TAG_STRING))
			storage.set(nbt.getString("Var"));
		else
			storage.set(null);
	}
}