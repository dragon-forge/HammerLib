package org.zeith.hammerlib.util.java;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.zeith.hammerlib.util.java.StreamHelper.*;

public class Codec<T>
{
	final Class<T> type;
	final List<Decoder<?, ?>> intermidiateDecoders = new ArrayList<>();
	final List<Decoder<?, T>> decoders = new ArrayList<>();
	
	public Codec(Class<T> type)
	{
		this.type = type;
		
		registerDecoder(decoder(type, Function.identity())); // Self-decoding first!
	}
	
	public Codec<T> registerIntermediateDecoder(Decoder<?, T> decoder)
	{
		intermidiateDecoders.add(decoder);
		return this;
	}
	
	public Codec<T> registerDecoder(Decoder<?, T> decoder)
	{
		decoders.add(decoder);
		return this;
	}
	
	public Optional<T> decode(Object in)
	{
		do
		{
			boolean decoded = false;
			for(Decoder<?, ?> id : intermidiateDecoders)
			{
				if(id.canDecode(in))
				{
					in = id.decode(in);
					decoded = true;
				}
			}
			if(!decoded) break;
		} while(true);
		
		return decoders.stream()
				.map(inject(in))
				.filter(filter(Decoder::canDecode))
				.map(transform(Decoder::decode))
				.findFirst();
	}
	
	public static <I, O> Decoder<I, O> decoder(Class<I> type, Function<I, O> handle)
	{
		return new Decoder<>(type, handle);
	}
	
	public static class Decoder<S, T>
	{
		final Class<S> handleType;
		final Predicate<S> decodable;
		final Function<S, T> decode;
		
		public Decoder(Class<S> handleType, Predicate<S> decodable, Function<S, T> decode)
		{
			this.handleType = handleType;
			this.decodable = decodable;
			this.decode = decode;
		}
		
		public Decoder(Class<S> handleType, Function<S, T> decode)
		{
			this.handleType = handleType;
			this.decodable = null;
			this.decode = decode;
		}
		
		public boolean canDecode(Object in)
		{
			return handleType.isAssignableFrom(in.getClass()) && (decodable == null || decodable.test(handleType.cast(in)));
		}
		
		public T decode(Object in)
		{
			return decode.apply(handleType.cast(in));
		}
	}
}