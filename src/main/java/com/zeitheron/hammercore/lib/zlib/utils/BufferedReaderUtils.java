/* Decompiled with CFR 0_123. */
package com.zeitheron.hammercore.lib.zlib.utils;

import java.io.BufferedReader;

import com.zeitheron.hammercore.lib.zlib.utils.IIntent;

public class BufferedReaderUtils
{
	public static void forEachLine(BufferedReader reader, IIntent intent)
	{
		try
		{
			String ln = reader.readLine();
			while(ln != null)
			{
				intent.run(0, ln);
				ln = reader.readLine();
			}
		} catch(Throwable ln)
		{
			// empty catch block
		}
	}
}
