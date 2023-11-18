package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(long[].class)
public class LongArraySerializer
		implements INBTSerializer<long[]>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull long[] value)
	{
		if(value != null)
			nbt.setTag(key, new NBTTagLongArray(value));
	}
	
	@Override
	public long[] deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_LONG_ARRAY) ? ((NBTTagLongArray) nbt.getTag(key)).data : null;
	}
}