package org.zeith.hammerlib.api.io;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Lets the object be {@link INBTSerializable} with defaulting serialization using HammerLib's {@link NBTSerializationHelper}
 */
public interface IAutoNBTSerializable
		extends ICompoundSerializable
{
	@Override
	default CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		return NBTSerializationHelper.serialize(provider, this);
	}
	
	@Override
	default void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		NBTSerializationHelper.deserialize(provider, this, nbt);
	}
}