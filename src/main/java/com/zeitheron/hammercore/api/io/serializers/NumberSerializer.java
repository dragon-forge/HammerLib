package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.utils.base.Cast;
import net.minecraft.nbt.*;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class NumberSerializer<N extends Number, NBT extends NBTBase>
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
	public void serialize(NBTTagCompound nbt, String key, @Nonnull N value)
	{
		if(value != null)
			nbt.setTag(key, encode.apply(value));
	}
	
	@Override
	public N deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, nbtType)) return decode.apply(Cast.cast(nbt.getTag(key)));
		return null;
	}
}