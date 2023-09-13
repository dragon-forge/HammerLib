package org.zeith.hammerlib.api.io.serializers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BaseCodecSerializer<T>
		implements INBTSerializer<T>
{
	protected final Codec<T> codec;
	protected final Supplier<T> defaultValue;
	
	public BaseCodecSerializer(Codec<T> codec, Supplier<T> defaultValue)
	{
		this.codec = codec;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void serialize(CompoundTag nbt, String key, @NotNull T value)
	{
		codec.encodeStart(NbtOps.INSTANCE, value)
				.result()
				.ifPresent(tag -> nbt.put(key, tag));
	}
	
	@Override
	public T deserialize(CompoundTag nbt, String key)
	{
		var tag = nbt.get(key);
		if(tag != null)
			return codec.decode(NbtOps.INSTANCE, tag)
					.result()
					.map(Pair::getFirst)
					.orElseGet(defaultValue);
		return defaultValue.get();
	}
}