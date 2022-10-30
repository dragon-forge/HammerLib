package org.zeith.hammerlib.util.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ByteHelper
{
	public static byte[][] divide(int max, byte[] data)
	{
		if(data.length == 0) return new byte[0][];
		
		List<byte[]> ld = new ArrayList<>();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		for(byte element : data)
		{
			baos.write(element);
			if(baos.size() >= max)
				ld.add(wrapAndReset(baos));
		}
		
		if(baos.size() >= 0)
			ld.add(wrapAndReset(baos));
		
		return ld.toArray(new byte[ld.size()][]);
	}
	
	public static byte[] wrapAndReset(ByteArrayOutputStream baos)
	{
		byte[] da = baos.toByteArray().clone();
		baos.reset();
		return da;
	}
	
	public static byte[] read(DataInputStream in) throws IOException
	{
		byte[] data = new byte[in.readInt()];
		in.readFully(data, 0, data.length);
		return data;
	}
	
	public static void write(DataOutputStream out, byte[] data) throws IOException
	{
		out.writeInt(data.length);
		out.write(data);
	}
}