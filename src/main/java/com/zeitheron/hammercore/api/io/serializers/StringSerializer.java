package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(String.class)
public class StringSerializer
		implements INBTSerializer<String>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull String value)
	{
		if(value != null)
			nbt.setString(key, value);
	}
	
	@Override
	public String deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_STRING) ? nbt.getString(key) : null;
	}
}