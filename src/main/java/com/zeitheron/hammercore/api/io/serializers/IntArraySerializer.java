package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(int[].class)
public class IntArraySerializer
		implements INBTSerializer<int[]>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull int[] value)
	{
		if(value != null)
			nbt.setIntArray(key, value);
	}
	
	@Override
	public int[] deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_INT_ARRAY) ? nbt.getIntArray(key) : null;
	}
}