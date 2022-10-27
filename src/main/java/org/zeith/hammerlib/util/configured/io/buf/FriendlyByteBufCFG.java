package org.zeith.hammerlib.util.configured.io.buf;

import net.minecraft.network.FriendlyByteBuf;

public class FriendlyByteBufCFG
		implements IByteBuf
{
	protected final FriendlyByteBuf buffer;
	
	public FriendlyByteBufCFG(FriendlyByteBuf buffer)
	{
		this.buffer = buffer;
	}
	
	@Override
	public void writeByte(int b)
	{
		buffer.writeByte((byte) b);
	}
	
	@Override
	public void writeShort(int s)
	{
		buffer.writeShort((short) s);
	}
	
	@Override
	public void writeInt(int i)
	{
		buffer.writeInt(i);
	}
	
	@Override
	public void writeLong(long l)
	{
		buffer.writeLong(l);
	}
	
	@Override
	public void writeFloat(float f)
	{
		buffer.writeFloat(f);
	}
	
	@Override
	public void writeDouble(double d)
	{
		buffer.writeDouble(d);
	}
	
	@Override
	public void writeBytes(byte[] bytes, int off, int len)
	{
		buffer.writeBytes(bytes, off, len);
	}
	
	@Override
	public byte readByte()
	{
		return buffer.readByte();
	}
	
	@Override
	public short readShort()
	{
		return buffer.readShort();
	}
	
	@Override
	public int readInt()
	{
		return buffer.readInt();
	}
	
	@Override
	public long readLong()
	{
		return buffer.readLong();
	}
	
	@Override
	public float readFloat()
	{
		return buffer.readFloat();
	}
	
	@Override
	public double readDouble()
	{
		return buffer.readDouble();
	}
	
	@Override
	public int readBytes(byte[] buf, int off, int length)
	{
		// if we don't have many bytes left, read whatever we can
		length = Math.min(length, buffer.readableBytes());
		buffer.readBytes(buf, off, length);
		return length;
	}
}