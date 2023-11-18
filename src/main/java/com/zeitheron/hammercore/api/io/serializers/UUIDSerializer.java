package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.UUID;

@NBTSerializer(UUID.class)
public class UUIDSerializer
		implements INBTSerializer<UUID>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull UUID value)
	{
		if(value != null)
			nbt.setUniqueId(key, value);
	}
	
	@Override
	public UUID deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_INT_ARRAY) ? nbt.getUniqueId(key) : null;
	}
}