package org.zeith.hammerlib.util.mcf;

import io.netty.buffer.*;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.consumers.Consumer2;

import java.nio.BufferUnderflowException;
import java.util.function.*;

public class ByteBufTransposer
{
	public static final byte[] EMPTY = new byte[0];
	
	public static <T> T read(byte[] data, Function<FriendlyByteBuf, T> reader)
	{
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
		try
		{
			return reader.apply(buf);
		} catch(ArrayIndexOutOfBoundsException | BufferUnderflowException e)
		{
			HammerLib.LOG.error("Failed to ByteBufTransposer.read:", e);
			return null;
		}
	}
	
	public static <T> byte[] transpose(T value, BiConsumer<T, FriendlyByteBuf> writer)
	{
		return transpose(buf -> writer.accept(value, buf));
	}
	
	public static <T> byte[] transpose(T value, Consumer2<FriendlyByteBuf, T> writer)
	{
		return transpose(buf -> writer.accept(buf, value));
	}
	
	public static byte[] transpose(Consumer<FriendlyByteBuf> writer)
	{
		return begin()
				.accept(writer)
				.transpose();
	}
	
	public static Builder begin()
	{
		return new Builder();
	}
	
	public static class Builder
	{
		protected final ByteBuf bb = Unpooled.buffer();
		protected final FriendlyByteBuf buf = new FriendlyByteBuf(bb);
		
		public FriendlyByteBuf buffer()
		{
			return buf;
		}
		
		public Builder accept(Consumer<FriendlyByteBuf> writer)
		{
			writer.accept(buf);
			return this;
		}
		
		public byte[] transpose()
		{
			int size = bb.writerIndex();
			if(size > 0)
			{
				bb.readerIndex(0);
				byte[] data = new byte[size];
				bb.readBytes(data);
				return data;
			}
			return EMPTY;
		}
	}
}