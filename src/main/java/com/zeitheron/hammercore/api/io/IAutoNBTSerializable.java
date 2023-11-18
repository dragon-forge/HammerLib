package com.zeitheron.hammercore.api.io;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Lets the object be {@link INBTSerializable} with defaulting serialization using HammerLib's {@link NBTSerializationHelper}
 */
public interface IAutoNBTSerializable
		extends INBTSerializable<NBTTagCompound>
{
	@Override
	default NBTTagCompound serializeNBT()
	{
		return NBTSerializationHelper.serialize(this);
	}

	@Override
	default void deserializeNBT(NBTTagCompound nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}
}