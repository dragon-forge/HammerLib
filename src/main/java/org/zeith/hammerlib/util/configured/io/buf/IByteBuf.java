package org.zeith.hammerlib.util.configured.io.buf;

import java.nio.charset.StandardCharsets;

public interface IByteBuf
{
	void writeByte(int b);
	
	void writeShort(int s);
	
	void writeInt(int i);
	
	void writeLong(long l);
	
	void writeFloat(float f);
	
	void writeDouble(double d);
	
	default void writeString(String str)
	{
		byte[] buf = str.getBytes(StandardCharsets.UTF_8);
		writeShort(buf.length);
		writeBytes(buf);
	}
	
	void writeBytes(byte[] bytes, int off, int len);
	
	default void writeBytes(byte[] bytes)
	{
		writeBytes(bytes, 0, bytes.length);
	}
	
	byte readByte();
	
	short readShort();
	
	int readInt();
	
	long readLong();
	
	float readFloat();
	
	double readDouble();
	
	default String readString()
	{
		byte[] buf = new byte[readShort()];
		int len = readBytes(buf);
		return new String(buf, 0, len, StandardCharsets.UTF_8);
	}
	
	int readBytes(byte[] buf, int off, int length);
	
	default int readBytes(byte[] buf)
	{
		return readBytes(buf, 0, buf.length);
	}
}