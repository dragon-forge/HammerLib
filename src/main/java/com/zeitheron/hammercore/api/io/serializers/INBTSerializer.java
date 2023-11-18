package com.zeitheron.hammercore.api.io.serializers;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.*;

public interface INBTSerializer<T>
{
	void serialize(NBTTagCompound nbt, String key, @Nonnull T value);
	
	@Nullable
	T deserialize(NBTTagCompound nbt, String key);
}