package com.zeitheron.hammercore.lib.zlib.io;

import java.io.IOException;
import java.io.InputStream;

public class VoidInputStream extends InputStream
{
	@Override
	public int read() throws IOException
	{
		return 0;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		return 0;
	}
	
	@Override
	public int read(byte[] b) throws IOException
	{
		return 0;
	}
}