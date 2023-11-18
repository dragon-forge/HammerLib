package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class BooleanSerializer<T extends Boolean>
		implements INBTSerializer<T>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull T value)
	{
		if(value != null)
			nbt.setBoolean(key, value);
	}
	
	@Override
	public T deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_BYTE) ? Cast.cast(nbt.getBoolean(key)) : Cast.cast(false);
	}
}