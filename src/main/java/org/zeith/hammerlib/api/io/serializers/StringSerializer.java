package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(String.class)
public class StringSerializer
		implements INBTSerializer<String>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull String value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putString(key, value);
	}

	@Override
	public String deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_STRING) ? nbt.getString(key) : null;
	}
}