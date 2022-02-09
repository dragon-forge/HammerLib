package org.zeith.hammerlib.api.io;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Lets the object be {@link INBTSerializable} with defaulting serialization using HammerLib's {@link NBTSerializationHelper}
 */
public interface IAutoNBTSerializable
		extends INBTSerializable<CompoundTag>
{
	@Override
	default CompoundTag serializeNBT()
	{
		return NBTSerializationHelper.serialize(this);
	}

	@Override
	default void deserializeNBT(CompoundTag nbt)
	{
		NBTSerializationHelper.deserialize(this, nbt);
	}
}