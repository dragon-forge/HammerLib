package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.Supplier;

@NBTSerializer(UUID.class)
public class UUIDSerializer
		implements INBTSerializer<UUID>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull UUID value)
	{
		if(value != null)
			nbt.setIntArray(key, encode(value));
	}
	
	@Override
	public UUID deserialize(NBTTagCompound nbt, String key)
	{
		int[] ints;
		return nbt.hasKey(key, Constants.NBT.TAG_INT_ARRAY) && (ints = nbt.getIntArray(key)).length == 4 ? decode(ints) : null;
	}
	
	public static int[] encode(UUID uuid)
	{
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		
		return new int[] {
				(int) (msb >>> 32),
				(int) msb,
				(int) (lsb >>> 32),
				(int) lsb
		};
	}
	
	public static UUID decode(int[] data)
	{
		if(data == null || data.length < 4) return null;
		long msb = ((long) data[0] << 32) | (data[1] & 0xFFFFFFFFL);
		long lsb = ((long) data[2] << 32) | (data[3] & 0xFFFFFFFFL);
		return new UUID(msb, lsb);
	}
	
	public static UUID decodeOrDefault(int[] data, UUID defaultValue)
	{
		if(data == null || data.length < 4) return defaultValue;
		long msb = ((long) data[0] << 32) | (data[1] & 0xFFFFFFFFL);
		long lsb = ((long) data[2] << 32) | (data[3] & 0xFFFFFFFFL);
		return new UUID(msb, lsb);
	}
	
	public static UUID decodeOrGet(int[] data, Supplier<UUID> defaultValue)
	{
		if(data == null || data.length < 4) return defaultValue.get();
		long msb = ((long) data[0] << 32) | (data[1] & 0xFFFFFFFFL);
		long lsb = ((long) data[2] << 32) | (data[3] & 0xFFFFFFFFL);
		return new UUID(msb, lsb);
	}
}