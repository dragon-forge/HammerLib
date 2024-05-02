package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.util.java.reflection.SerializableMethodHandle;

@NBTSerializer(SerializableMethodHandle.class)
public class SerializableMethodHandleSerializer
		implements INBTSerializer<SerializableMethodHandle>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull SerializableMethodHandle value, CompoundTag nbt)
	{
		if(value != null)
			nbt.put(key, value.serializeNBT(provider));
	}
	
	@Override
	public @Nullable SerializableMethodHandle deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		var handle = nbt.contains(key, Tag.TAG_COMPOUND)
					 ? new SerializableMethodHandle(provider, nbt.getCompound(key))
					 : null;
		
		return handle != null && handle.isResolved() ? handle : null;
	}
}