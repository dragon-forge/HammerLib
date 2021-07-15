package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(int[].class)
public class IntArraySerializer
		implements INBTSerializer<int[]>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, int[] value)
	{
		if(value != null)
			nbt.putIntArray(key, value);
	}

	@Override
	public int[] deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_INT_ARRAY) ? nbt.getIntArray(key) : null;
	}
}