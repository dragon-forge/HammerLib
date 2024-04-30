package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(byte[].class)
public class ByteArraySerializer
		implements INBTSerializer<byte[]>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, byte @NotNull [] value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putByteArray(key, value);
	}

	@Override
	public byte[] deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_BYTE_ARRAY) ? nbt.getByteArray(key) : null;
	}
}