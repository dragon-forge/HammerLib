package org.zeith.hammerlib.api.io;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Lets the object be {@link INBTSerializable} with defaulting serialization using HammerLib's {@link NBTSerializationHelper}
 */
public interface IAutoNBTSerializable
		extends INBTSerializable<CompoundNBT>
{
	@Override
	default CompoundNBT serializeNBT()
	{
		return NBTSerializationHelper.serialize(this);
	}

	@Override
	default void deserializeNBT(CompoundNBT nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}
}