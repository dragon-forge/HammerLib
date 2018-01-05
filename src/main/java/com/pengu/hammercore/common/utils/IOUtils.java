package com.pengu.hammercore.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.function.Supplier;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;

import com.endie.lib.io.MultiOutputStream;
import com.endie.lib.io.cache.iCacher;
import com.endie.lib.tuple.TwoTuple;
import com.endie.lib.utils.Joiner;
import com.pengu.hammercore.core.HammerCoreCacher;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONTokener;

public class IOUtils
{
	public static iCacher cache = new HammerCoreCacher();
	public static final DecimalFormat lenform = new DecimalFormat("#0.00");
	public static int heapLimit = 4096;
	public static final byte[] ZERO_ARRAY = new byte[0];
	private static final ThreadLocal<byte[]> buf = ThreadLocal.withInitial(new Supplier<byte[]>()
	{
		
		@Override
		public byte[] get()
		{
			return new byte[IOUtils.heapLimit];
		}
	});
	
	public static Object jsonparse(File file) throws JSONException
	{
		return new JSONTokener(ioget(file)).nextValue();
	}
	
	public static Object downloadjson(String url) throws JSONException
	{
		return new JSONTokener(ioget(url)).nextValue();
	}
	
	public static String ioget(File file)
	{
		if(file.isFile())
			try
			{
				return Joiner.NEW_LINE.join(Files.readAllLines(file.toPath()));
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		return "";
	}
	
	public static String ioget(String url)
	{
		return new String(downloadData(url));
	}
	
	public static BufferedImage downloadPicture(String url)
	{
		try
		{
			return ImageIO.read(new URL(url));
		} catch(Throwable throwable)
		{
			return null;
		}
	}
	
	public static byte[] downloadData(String url)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.downloadAndWriteData(url, baos);
			byte[] buf = baos.toByteArray();
			return buf;
		} catch(Throwable baos)
		{
			return ZERO_ARRAY;
		}
	}
	
	public static void downloadAndWriteData(String url, OutputStream o)
	{
		InputStream internet = null;
		boolean live = false;
		
		if(!cache.preventLiveConnection(url))
			try
			{
				URL u = new URL(url);
				internet = u.openStream();
				live = true;
			} catch(Throwable err)
			{
				// No internet?
			}
		
		if(internet == null && cache.isActuallyWorking())
			internet = cache.pull(url);
		
		if(internet != null)
			try
			{
				if(live)
				{
					OutputStream ao = cache.isActuallyWorking() ? cache.put(url) : null;
					if(ao == null)
						IOUtils.pipeData(internet, o);
					else
					{
						IOUtils.pipeData(internet, new MultiOutputStream(o, ao));
						ao.close();
					}
				} else
					IOUtils.pipeData(internet, o);
				internet.close();
			} catch(Throwable u)
			{
				// empty catch block
			}
	}
	
	public static void pipeData(InputStream from, OutputStream to)
	{
		try
		{
			byte[] buf = IOUtils.buf.get();
			int read = 0;
			while((read = from.read(buf)) > 0)
				to.write(buf, 0, read);
		} catch(Throwable buf)
		{
			// empty catch block
		}
	}
	
	public static byte[] pipeOut(InputStream from)
	{
		ByteArrayOutputStream to = new ByteArrayOutputStream();
		IOUtils.pipeData(from, to);
		return to.toByteArray();
	}
	
	public static byte[] pipeOutAvaliable(InputStream from)
	{
		try
		{
			byte[] buf = new byte[from.available()];
			from.read(buf);
			return buf;
		} catch(Throwable buf)
		{
			return ZERO_ARRAY;
		}
	}
	
	public static byte[] deflaterCompress(byte[] data)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DeflaterOutputStream o = new DeflaterOutputStream((OutputStream) baos, new Deflater(9));
			o.write(data);
			o.close();
			return baos.toByteArray();
		} catch(Throwable baos)
		{
			return ZERO_ARRAY;
		}
	}
	
	public static byte[] deflaterUncompress(byte[] data)
	{
		try
		{
			InflaterInputStream i = new InflaterInputStream(new ByteArrayInputStream(data), new Inflater());
			data = IOUtils.pipeOut(i);
			i.close();
			return data;
		} catch(Throwable i)
		{
			return ZERO_ARRAY;
		}
	}
	
	public static TwoTuple<InputStream, Boolean> getInput(String url)
	{
		InputStream internet = null;
		boolean live = false;
		
		if(!cache.preventLiveConnection(url))
			try
			{
				URL u = new URL(url);
				internet = u.openStream();
				live = true;
			} catch(Throwable err)
			{
				// No internet?
			}
		
		if(internet == null)
			internet = cache.pull(url);
		
		return new TwoTuple<>(internet, live);
	}
	
	public static long size(File file)
	{
		if(file.isFile())
			try
			{
				return Files.size(file.toPath());
			} catch(Throwable err)
			{
			}
		long size = 0L;
		if(file.isDirectory())
			for(File f : file.listFiles())
				size += size(f);
		return size;
	}
	
	public static String getFormattedFileSize(long size)
	{
		if(size == -1L)
			return "?";
		
		long B = size;
		double KB = size / 1024D;
		double MB = KB / 1024D;
		double GB = MB / 1024D;
		double TB = GB / 1024D;
		
		return (B < 1024L ? B + " B" : KB < 1024D ? lenform.format(KB) + " KB" : MB < 1024D ? lenform.format(MB) + " MB" : GB < 1024D ? lenform.format(GB) + " GB" : lenform.format(TB) + " TB");
	}
	
	/**
	 * Follows redirect links (may be useful when downloading a file)
	 * 
	 * @since 1.5.2
	 */
	public static String followRedirects(String url) throws IOException
	{
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setReadTimeout(5000);
		conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
		conn.addRequestProperty("User-Agent", "Mozilla");
		conn.addRequestProperty("Referer", "google.com");
		
		boolean redirect = false;
		
		// normally, 3xx is redirect
		int status = conn.getResponseCode();
		if(status != HttpURLConnection.HTTP_OK)
		{
			if(status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
		}
		
		if(redirect)
		{
			// get redirect url from "location" header field
			String newUrl = conn.getHeaderField("Location");
			
			// get the cookie if need, for login
			String cookies = conn.getHeaderField("Set-Cookie");
			
			// open the new connnection again
			conn = (HttpURLConnection) new URL(newUrl).openConnection();
			conn.setRequestProperty("Cookie", cookies);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");
		}
		
		String u = conn.getHeaderField("Location");
		return u != null ? u : url;
	}
}