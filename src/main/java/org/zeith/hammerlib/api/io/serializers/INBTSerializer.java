package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;

public interface INBTSerializer<T>
{
	void serialize(CompoundNBT nbt, String key, T value);

	T deserialize(CompoundNBT nbt, String key);
}