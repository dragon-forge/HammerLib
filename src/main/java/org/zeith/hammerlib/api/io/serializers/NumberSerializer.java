package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Function;

public class NumberSerializer<N extends Number, NBT extends INBT>
		implements INBTSerializer<N>
{
	final int nbtType;
	final Function<N, NBT> encode;
	final Function<NBT, N> decode;

	public NumberSerializer(int nbtType, Function<N, NBT> encode, Function<NBT, N> decode)
	{
		this.nbtType = nbtType;
		this.encode = encode;
		this.decode = decode;
	}

	@Override
	public void serialize(CompoundNBT nbt, String key, N value)
	{
		if(value != null)
			nbt.put(key, encode.apply(value));
	}

	@Override
	public N deserialize(CompoundNBT nbt, String key)
	{
		if(nbt.contains(key, nbtType)) return decode.apply(Cast.cast(nbt.get(key)));
		return null;
	}
}