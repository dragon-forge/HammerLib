package org.zeith.hammerlib.util.configured.io.buf;

import org.zeith.hammerlib.util.configured.io.IoRunnable;

import java.io.*;
import java.util.function.Consumer;

public class WritingBuf
		implements IByteBuf
{
	private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	private final DataOutputStream out = new DataOutputStream(bytes);
	
	public WritingBuf()
	{
		bytes.reset();
	}
	
	public byte[] getAndReset()
	{
		byte[] buf = bytes.toByteArray();
		bytes.reset();
		return buf;
	}
	
	private void safeRun(IoRunnable task)
	{
		try
		{
			task.run();
		} catch(IOException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public void writeByte(int b)
	{
		safeRun(() -> out.writeByte(b));
	}
	
	@Override
	public void writeShort(int s)
	{
		safeRun(() -> out.writeShort(s));
	}
	
	@Override
	public void writeInt(int i)
	{
		safeRun(() -> out.writeInt(i));
	}
	
	@Override
	public void writeLong(long l)
	{
		safeRun(() -> out.writeLong(l));
	}
	
	@Override
	public void writeFloat(float f)
	{
		safeRun(() -> out.writeFloat(f));
	}
	
	@Override
	public void writeDouble(double d)
	{
		safeRun(() -> out.writeDouble(d));
	}
	
	@Override
	public void writeBytes(byte[] bytes, int off, int len)
	{
		safeRun(() -> out.write(bytes, off, len));
	}
	
	@Override
	public byte readByte()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public short readShort()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int readInt()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public long readLong()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public float readFloat()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public double readDouble()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int readBytes(byte[] buf, int off, int length)
	{
		throw new UnsupportedOperationException();
	}
	
	public static byte[] encode(Consumer<IByteBuf> writer)
	{
		var buf = new WritingBuf();
		writer.accept(buf);
		return buf.getAndReset();
	}
}