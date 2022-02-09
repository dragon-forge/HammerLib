package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;

public interface INBTSerializer<T>
{
	void serialize(CompoundTag nbt, String key, T value);

	T deserialize(CompoundTag nbt, String key);
}