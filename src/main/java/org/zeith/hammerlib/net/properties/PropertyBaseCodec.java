package org.zeith.hammerlib.net.properties;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketBuffer;
import org.zeith.hammerlib.util.java.*;

import java.util.*;
import java.util.function.Supplier;

public class PropertyBaseCodec<T>
		extends PropertyBase<T>
{
	protected final Codec<T> codec;
	protected final Supplier<T> defaultValue;
	
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
	public void write(PacketBuffer buf)
	{
		T value = this.value.get();
		
		Optional<CompoundNBT> res =
				value == null
				? Optional.empty()
				: codec.encodeStart(NBTDynamicOps.INSTANCE, value)
						.result()
						.flatMap(tag -> Cast.optionally(tag, CompoundNBT.class));
		
		buf.writeBoolean(res.isPresent());
		if(res.isPresent()) buf.writeNbt(res.orElseThrow(NoSuchElementException::new));
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		if(buf.readBoolean())
		{
			this.value.set(codec.decode(NBTDynamicOps.INSTANCE, buf.readNbt())
					.result()
					.map(Pair::getFirst)
					.orElseGet(defaultValue));
		} else
			this.value.set(defaultValue.get());
	}
}
