package org.zeith.hammerlib.api.io.serializers.codec;

import com.mojang.serialization.Codec;
import org.zeith.hammerlib.api.io.serializers.BaseCodecSerializer;
import org.zeith.hammerlib.api.io.serializers.INBTSerializer;
import org.zeith.hammerlib.net.properties.PropertyBaseCodec;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.function.Supplier;

public interface ICodecSerializer<T>
{
	Class<T> type();
	
	Codec<T> codec();
	
	T defaultValue();
	
	default INBTSerializer<T> asSerializer()
	{
		return new BaseCodecSerializer<>(codec(), this::defaultValue);
	}
	
	default PropertyBaseCodec<T> asProperty()
	{
		return new PropertyBaseCodec<>(this);
	}
	
	default PropertyBaseCodec<T> asProperty(DirectStorage<T> storage)
	{
		return new PropertyBaseCodec<>(this, storage);
	}
	
	static <T> ICodecSerializer<T> of(Class<T> type, Codec<T> codec)
	{
		return of(type, codec, Cast.constant(null));
	}
	
	static <T> ICodecSerializer<T> of(Class<T> type, Codec<T> codec, Supplier<T> defaultValue)
	{
		return new ICodecSerializer<>()
		{
			@Override
			public Class<T> type()
			{
				return type;
			}
			
			@Override
			public Codec<T> codec()
			{
				return codec;
			}
			
			@Override
			public T defaultValue()
			{
				return defaultValue.get();
			}
		};
	}
}