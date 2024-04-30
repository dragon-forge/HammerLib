package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Cast;

public class BooleanSerializer<T extends Boolean>
		implements INBTSerializer<T>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull T value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putBoolean(key, value);
	}

	@Override
	public T deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_BYTE) ? Cast.cast(nbt.getBoolean(key)) : Cast.cast(false);
	}
}