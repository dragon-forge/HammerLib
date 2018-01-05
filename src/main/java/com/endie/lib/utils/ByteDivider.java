package com.endie.lib.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ByteDivider
{
	public static byte[][] divide(int max, byte[] data)
	{
		List<byte[]> ld = new ArrayList<>();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		for(int i = 0; i < data.length; ++i)
		{
			baos.write(data[i]);
			if(baos.size() >= max)
			{
				ld.add(baos.toByteArray().clone());
				baos.reset();
			}
		}
		
		if(baos.size() >= 0)
		{
			ld.add(baos.toByteArray().clone());
			baos.reset();
		}
		
		return ld.toArray(new byte[ld.size()][]);
	}
}