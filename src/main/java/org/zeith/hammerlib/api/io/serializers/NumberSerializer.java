package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Function;

public class NumberSerializer<N extends Number, NBT extends Tag>
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
	public void serialize(HolderLookup.Provider provider, String key, @NotNull N value, CompoundTag nbt)
	{
		if(value != null)
			nbt.put(key, encode.apply(value));
	}

	@Override
	public N deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		if(nbt.contains(key, nbtType)) return decode.apply(Cast.cast(nbt.get(key)));
		return null;
	}
}