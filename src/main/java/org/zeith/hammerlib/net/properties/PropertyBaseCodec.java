package org.zeith.hammerlib.net.properties;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.api.io.serializers.codec.ICodecSerializer;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Optional;
import java.util.function.Supplier;

public class PropertyBaseCodec<T>
		extends PropertyBase<T>
{
	protected final Codec<T> codec;
	protected final Supplier<T> defaultValue;
	
	
	public PropertyBaseCodec(ICodecSerializer<T> serializer, DirectStorage<T> value)
	{
		this(serializer.codec(), serializer::defaultValue, serializer.type(), value);
	}
	
	public PropertyBaseCodec(ICodecSerializer<T> serializer)
	{
		this(serializer.codec(), serializer::defaultValue, serializer.type());
	}
	
	
	
	public PropertyBaseCodec(Codec<T> codec, Supplier<T> defaultValue, Class<T> type, DirectStorage<T> value)
	{
		super(type, value);
		this.codec = codec;
		this.defaultValue = defaultValue;
	}
	
	public PropertyBaseCodec(Codec<T> codec, Supplier<T> defaultValue, Class<T> type)
	{
		super(type);
		this.codec = codec;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		T value = this.value.get();
		
		Optional<CompoundTag> res =
				value == null
				? Optional.empty()
				: codec.encodeStart(NbtOps.INSTANCE, value)
						.result()
						.flatMap(tag -> Cast.optionally(tag, CompoundTag.class));
		
		buf.writeBoolean(res.isPresent());
		if(res.isPresent()) buf.writeNbt(res.orElseThrow());
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		if(buf.readBoolean())
		{
			this.value.set(codec.decode(NbtOps.INSTANCE, buf.readNbt())
					.result()
					.map(Pair::getFirst)
					.orElseGet(defaultValue));
		} else
			this.value.set(defaultValue.get());
	}
}
