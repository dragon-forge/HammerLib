package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(int[].class)
public class IntArraySerializer
		implements INBTSerializer<int[]>
{
	@Override
	public void serialize(CompoundTag nbt, String key, int[] value)
	{
		if(value != null)
			nbt.putIntArray(key, value);
	}

	@Override
	public int[] deserialize(CompoundTag nbt, String key)
	{
		return nbt.contains(key, Tag.TAG_INT_ARRAY) ? nbt.getIntArray(key) : null;
	}
}