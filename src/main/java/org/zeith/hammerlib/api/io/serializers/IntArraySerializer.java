package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(int[].class)
public class IntArraySerializer
		implements INBTSerializer<int[]>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, int @NotNull [] value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putIntArray(key, value);
	}

	@Override
	public int[] deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_INT_ARRAY) ? nbt.getIntArray(key) : null;
	}
}