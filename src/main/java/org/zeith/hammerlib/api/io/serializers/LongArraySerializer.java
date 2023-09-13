package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(long[].class)
public class LongArraySerializer
		implements INBTSerializer<long[]>
{
	@Override
	public void serialize(CompoundTag nbt, String key, long @NotNull [] value)
	{
		if(value != null)
			nbt.putLongArray(key, value);
	}

	@Override
	public long[] deserialize(CompoundTag nbt, String key)
	{
		return nbt.contains(key, Tag.TAG_LONG_ARRAY) ? nbt.getLongArray(key) : null;
	}
}