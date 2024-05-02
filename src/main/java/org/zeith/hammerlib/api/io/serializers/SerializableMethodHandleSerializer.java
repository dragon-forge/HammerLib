package org.zeith.hammerlib.api.io.serializers;

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
	public void serialize(CompoundTag nbt, String key, @NotNull SerializableMethodHandle value)
	{
		if(value != null)
			nbt.put(key, value.serializeNBT());
	}
	
	@Override
	public @Nullable SerializableMethodHandle deserialize(CompoundTag nbt, String key)
	{
		var handle = nbt.contains(key, Tag.TAG_COMPOUND)
					 ? new SerializableMethodHandle(nbt.getCompound(key))
					 : null;
		
		return handle != null && handle.isResolved() ? handle : null;
	}
}