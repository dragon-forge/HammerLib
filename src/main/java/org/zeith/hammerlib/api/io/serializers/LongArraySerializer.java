package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(long[].class)
public class LongArraySerializer
		implements INBTSerializer<long[]>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, long @NotNull [] value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putLongArray(key, value);
	}

	@Override
	public long[] deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_LONG_ARRAY) ? nbt.getLongArray(key) : null;
	}
}