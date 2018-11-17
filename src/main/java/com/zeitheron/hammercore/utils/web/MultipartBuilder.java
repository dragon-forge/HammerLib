package com.zeitheron.hammercore.utils.web;

import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MultipartBuilder
{
	public final String bound;
	public final List<MultipartEntity> parts = new ArrayList<>();
	
	{
		char[] available = "0123456789abcdefghijABCDEFGHIJ".toCharArray();
		String bound = "----HammerCoreFormBoundary-";
		for(int i = 0; i < available.length; ++i)
			bound += available[ThreadLocalRandom.current().nextInt(available.length)];
		this.bound = bound;
	}
	
	public MultipartBuilder addPart(MultipartEntity entity)
	{
		parts.add(entity);
		return this;
	}
	
	public MultipartBuilder addPart(String name, String text)
	{
		try
		{
			return addPart(new MultipartEntity(name, null, "text/txt", text.getBytes("UFT-8")));
		} catch(UnsupportedEncodingException e)
		{
			return addPart(new MultipartEntity(name, null, "text/txt", text.getBytes()));
		}
	}
	
	public void writeMultipart(OutputStream target) throws IOException
	{
		for(MultipartEntity entity : parts)
			entity.write(this, target);
		target.write(("\r\n--" + bound + "--").getBytes());
	}
	
	public static class MultipartEntity
	{
		public final String name, filename, contentType;
		public final byte[] data;
		
		public MultipartEntity(String name, String filename, String contentType, byte[] data)
		{
			this.name = name;
			this.filename = filename;
			this.contentType = contentType == null || contentType.isEmpty() ? "*/*" : contentType;
			this.data = data;
		}
		
		public void write(MultipartBuilder builder, OutputStream output) throws IOException
		{
			String info = "--" + builder.bound + "\r\nContent-Disposition: form-data; name=\"" + name + "\";" + (filename != null ? (" filename=\"" + filename + "\"") : "") + "\r\nContent-Type: " + contentType + "\r\n\r\n";
			
			output.write(info.getBytes());
			output.write(data);
		}
	}
}