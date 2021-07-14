package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class LongArraySerializer
		implements INBTSerializer<long[]>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, long[] value)
	{
		if(value != null)
			nbt.putLongArray(key, value);
	}

	@Override
	public long[] deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_LONG_ARRAY) ? nbt.getLongArray(key) : null;
	}
}