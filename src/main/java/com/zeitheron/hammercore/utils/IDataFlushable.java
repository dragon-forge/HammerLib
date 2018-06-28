package com.zeitheron.hammercore.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;

public interface IDataFlushable
{
	void load(DataInputStream in) throws IOException;
	
	void save(DataOutputStream out) throws IOException;
	
	public static class IO
	{
		public static IDataFlushable readFlushable(DataInputStream in) throws IOException, ClassNotFoundException
		{
			byte[] buf = new byte[in.readShort()];
			in.readFully(buf);
			
			try
			{
				Constructor<? extends IDataFlushable> data = (Constructor<? extends IDataFlushable>) Class.forName(new String(buf)).getConstructor();
				data.setAccessible(true);
				IDataFlushable fl = data.newInstance();
				fl.load(in);
				return fl;
			} catch(SecurityException | ReflectiveOperationException | IllegalArgumentException e)
			{
				throw new IOException(e);
			}
		}
		
		public static void writeFlushable(DataOutputStream out, IDataFlushable fl) throws IOException
		{
			byte[] buf = fl.getClass().getName().getBytes();
			out.writeShort(buf.length);
			out.write(buf);
			fl.save(out);
		}
		
		public static void writeStringB(DataOutputStream dos, String str) throws IOException
		{
			byte[] data = str.getBytes();
			if(data.length > Byte.MAX_VALUE)
				return;
			dos.writeByte(data.length);
			dos.write(data);
		}
		
		public static void writeStringS(DataOutputStream dos, String str) throws IOException
		{
			byte[] data = str.getBytes();
			if(data.length > Short.MAX_VALUE)
				return;
			dos.writeShort(data.length);
			dos.write(data);
		}
		
		public static void writeString(DataOutputStream dos, String str) throws IOException
		{
			byte[] data = str.getBytes();
			dos.writeInt(data.length);
			dos.write(data);
		}
		
		public static String readStringB(DataInputStream dis) throws IOException
		{
			byte[] buf = new byte[dis.readByte()];
			dis.readFully(buf);
			return new String(buf);
		}
		
		public static String readStringS(DataInputStream dis) throws IOException
		{
			byte[] buf = new byte[dis.readShort()];
			dis.readFully(buf);
			return new String(buf);
		}
		
		public static String readString(DataInputStream dis) throws IOException
		{
			byte[] buf = new byte[dis.readInt()];
			dis.readFully(buf);
			return new String(buf);
		}
	}
}