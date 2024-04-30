package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

import java.util.UUID;

@NBTSerializer(UUID.class)
public class UUIDSerializer
		implements INBTSerializer<UUID>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull UUID value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putUUID(key, value);
	}

	@Override
	public UUID deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_INT_ARRAY) ? nbt.getUUID(key) : null;
	}
}