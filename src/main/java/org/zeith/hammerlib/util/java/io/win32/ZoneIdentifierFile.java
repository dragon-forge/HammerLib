package org.zeith.hammerlib.util.java.io.win32;

import java.io.*;
import java.nio.file.*;
import java.util.function.LongConsumer;

public final class ZoneIdentifierFile
		extends File
{
	private final File origin;
	
	ZoneIdentifierFile(File origin, String parent)
	{
		super(parent);
		this.origin = origin;
	}
	
	public File getOrigin()
	{
		return origin;
	}
	
	public long downloadFile(InputStream input, String websiteUrl, String directUrl) throws IOException
	{
		long r = Files.copy(input, origin.toPath(), StandardCopyOption.REPLACE_EXISTING);
		ZoneIdentifier id = new ZoneIdentifier();
		id.zoneId = 3;
		id.referrerUrl = websiteUrl;
		id.hostUrl = directUrl;
		set(id);
		return r;
	}
	
	public void downloadFile(InputStream input, String websiteUrl, String directUrl, LongConsumer bytesDownloaded) throws IOException
	{
		Files.deleteIfExists(origin.toPath());
		
		try(OutputStream output = Files.newOutputStream(origin.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))
		{
			long nread = 0L;
			byte[] buf = new byte[8192];
			int n;
			while((n = input.read(buf)) > 0)
			{
				output.write(buf, 0, n);
				nread += n;
				bytesDownloaded.accept(nread);
			}
		}
		
		ZoneIdentifier id = new ZoneIdentifier();
		id.zoneId = 3;
		id.referrerUrl = websiteUrl;
		id.hostUrl = directUrl;
		set(id);
	}
	
	public void set(String websiteUrl, String directUrl) throws IOException
	{
		ZoneIdentifier id = new ZoneIdentifier();
		id.zoneId = 3;
		id.referrerUrl = websiteUrl;
		id.hostUrl = directUrl;
		set(id);
	}
	
	public boolean set(ZoneIdentifier id) throws IOException
	{
		if(origin.isFile())
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(this)))
			{
				id.write(writer);
				return true;
			} catch(FileNotFoundException e)
			{
				return false;
			}
		return false;
	}
}