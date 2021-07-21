package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

import java.util.UUID;

@NBTSerializer(UUID.class)
public class UUIDSerializer
		implements INBTSerializer<UUID>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, UUID value)
	{
		if(value != null)
			nbt.putUUID(key, value);
	}

	@Override
	public UUID deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_INT_ARRAY) ? nbt.getUUID(key) : null;
	}
}