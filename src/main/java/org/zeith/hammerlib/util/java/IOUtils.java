package org.zeith.hammerlib.util.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils
{
	public static void pipeData(InputStream in, OutputStream out) throws IOException
	{
		byte[] buf = new byte[1024];
		int r;
		while((r = in.read(buf)) > 0) out.write(buf, 0, r);
	}

	public static byte[] pipeOut(InputStream in) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pipeData(in, baos);
		return baos.toByteArray();
	}
}