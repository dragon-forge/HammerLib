package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class StringSerializer
		implements INBTSerializer<String>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, String value)
	{
		if(value != null)
			nbt.putString(key, value);
	}

	@Override
	public String deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_STRING) ? nbt.getString(key) : null;
	}
}