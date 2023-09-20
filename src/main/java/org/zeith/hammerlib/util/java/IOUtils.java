package org.zeith.hammerlib.util.java;

import java.io.*;
import java.nio.file.*;

public class IOUtils
{
	public static void pipeData(InputStream in, OutputStream out)
			throws IOException
	{
		byte[] buf = new byte[1024];
		int r;
		while((r = in.read(buf)) > 0) out.write(buf, 0, r);
	}
	
	public static byte[] pipeOut(InputStream in)
			throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pipeData(in, baos);
		return baos.toByteArray();
	}
	
	public static void deleteFolder(Path file)
			throws IOException
	{
		Files.walk(file)
				.filter(Files::isRegularFile)
				.map(Path::toFile)
				.forEach(File::delete);
		deleteDirs(file.toFile());
	}
	
	private static void deleteDirs(File dir)
	{
		if(dir.isDirectory())
			for(File file : dir.listFiles())
				deleteDirs(file);
		dir.delete();
	}
}