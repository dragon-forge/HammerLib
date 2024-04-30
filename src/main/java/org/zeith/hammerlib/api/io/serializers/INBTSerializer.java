package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface INBTSerializer<T>
{
	void serialize(HolderLookup.Provider provider, String key, @NotNull T value, CompoundTag nbt);
	
	@Nullable
	T deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt);
}