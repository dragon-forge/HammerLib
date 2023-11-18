package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(byte[].class)
public class ByteArraySerializer
		implements INBTSerializer<byte[]>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull byte[] value)
	{
		if(value != null)
			nbt.setByteArray(key, value);
	}
	
	@Override
	public byte[] deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_BYTE_ARRAY) ? nbt.getByteArray(key) : null;
	}
}