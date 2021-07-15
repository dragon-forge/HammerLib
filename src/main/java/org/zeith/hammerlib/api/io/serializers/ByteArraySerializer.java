package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(byte[].class)
public class ByteArraySerializer
		implements INBTSerializer<byte[]>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, byte[] value)
	{
		if(value != null)
			nbt.putByteArray(key, value);
	}

	@Override
	public byte[] deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_BYTE_ARRAY) ? nbt.getByteArray(key) : null;
	}
}