package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.*;

public interface INBTSerializer<T>
{
	void serialize(CompoundTag nbt, String key, @NotNull T value);
	
	@Nullable
	T deserialize(CompoundTag nbt, String key);
}