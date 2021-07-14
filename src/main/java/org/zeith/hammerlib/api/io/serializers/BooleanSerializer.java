package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.util.java.Cast;

public class BooleanSerializer<T extends Boolean>
		implements INBTSerializer<T>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, T value)
	{
		if(value != null)
			nbt.putBoolean(key, value);
	}

	@Override
	public T deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_BYTE) ? Cast.cast(nbt.getBoolean(key)) : Cast.cast(false);
	}
}