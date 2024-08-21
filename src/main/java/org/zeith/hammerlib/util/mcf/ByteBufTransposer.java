package org.zeith.hammerlib.util.mcf;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.utils.java.consumers.Consumer2;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;

import java.nio.BufferUnderflowException;
import java.util.function.*;

public class ByteBufTransposer
{
	public static final byte[] EMPTY = new byte[0];
	
	public static <T> T read(byte[] data, Function<PacketBuffer, T> reader)
	{
		PacketBuffer buf = new PacketBuffer(Unpooled.wrappedBuffer(data));
		try
		{
			return reader.apply(buf);
		} catch(ArrayIndexOutOfBoundsException | BufferUnderflowException e)
		{
			HammerCore.LOG.error("Failed to ByteBufTransposer.read:", e);
			return null;
		}
	}
	
	public static <T> byte[] transpose(T value, BiConsumer<T, PacketBuffer> writer)
	{
		return transpose(buf -> writer.accept(value, buf));
	}
	
	public static <T> byte[] transpose(T value, Consumer2<PacketBuffer, T> writer)
	{
		return transpose(buf -> writer.accept(buf, value));
	}
	
	public static byte[] transpose(Consumer<PacketBuffer> writer)
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
		protected final PacketBuffer buf = new PacketBuffer(bb);
		
		public ByteBuf rawBuffer()
		{
			return bb;
		}
		
		public PacketBuffer buffer()
		{
			return buf;
		}
		
		public Builder accept(Consumer<PacketBuffer> writer)
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