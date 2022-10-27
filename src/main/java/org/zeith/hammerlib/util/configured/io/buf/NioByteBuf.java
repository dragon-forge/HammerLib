package org.zeith.hammerlib.util.configured.io.buf;

import java.nio.ByteBuffer;

public class NioByteBuf
		implements IByteBuf
{
	protected final ByteBuffer buffer;
	
	public NioByteBuf(ByteBuffer buffer)
	{
		this.buffer = buffer;
	}
	
	@Override
	public void writeByte(int b)
	{
		buffer.put((byte) b);
	}
	
	@Override
	public void writeShort(int s)
	{
		buffer.putShort((short) s);
	}
	
	@Override
	public void writeInt(int i)
	{
		buffer.putInt(i);
	}
	
	@Override
	public void writeLong(long l)
	{
		buffer.putLong(l);
	}
	
	@Override
	public void writeFloat(float f)
	{
		buffer.putFloat(f);
	}
	
	@Override
	public void writeDouble(double d)
	{
		buffer.putDouble(d);
	}
	
	@Override
	public void writeBytes(byte[] bytes, int off, int len)
	{
		buffer.put(bytes, off, len);
	}
	
	@Override
	public byte readByte()
	{
		return buffer.get();
	}
	
	@Override
	public short readShort()
	{
		return buffer.getShort();
	}
	
	@Override
	public int readInt()
	{
		return buffer.getInt();
	}
	
	@Override
	public long readLong()
	{
		return buffer.getLong();
	}
	
	@Override
	public float readFloat()
	{
		return buffer.getFloat();
	}
	
	@Override
	public double readDouble()
	{
		return buffer.getDouble();
	}
	
	@Override
	public int readBytes(byte[] buf, int off, int length)
	{
		// if we don't have many bytes left, read whatever we can
		length = Math.min(length, buffer.limit() - buffer.position());
		buffer.get(buf, off, length);
		return length;
	}
}