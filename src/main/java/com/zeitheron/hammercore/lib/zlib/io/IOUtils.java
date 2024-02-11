package com.zeitheron.hammercore.lib.zlib.io;

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

import com.zeitheron.hammercore.HLConstants;
import com.zeitheron.hammercore.utils.forge.ModHelper;
import org.apache.http.client.methods.HttpOptions;

import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.io.cache.ICacher;
import com.zeitheron.hammercore.lib.zlib.io.cache.VoidCacher;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;
import com.zeitheron.hammercore.lib.zlib.utils.Joiner;
import com.zeitheron.hammercore.lib.zlib.web.HttpRequest;
import com.zeitheron.hammercore.utils.classes.ClassWrapper;

public class IOUtils
{
	public static final ICacher cache = new VoidCacher();
	public static final DecimalFormat lenform = new DecimalFormat("#0.00");
	public static int heapLimit = 4096;
	public static final byte[] ZERO_ARRAY = new byte[0];
	
	public static Object jsonparse(File file) throws JSONException
	{
		return new JSONTokener(ioget(file)).nextValue();
	}
	
	public static Object downloadjson(String url) throws JSONException
	{
		return new JSONTokener(ioget(url)).nextValue();
	}
	
	public static Object downloadjsonOrLoadFromInternal(String url, String path) throws JSONException
	{
		String data = ioget(url);
		if(data.isEmpty())
			data = new String(pipeOut(ClassWrapper.getCallerClass().getResourceAsStream(path)));
		return data.isEmpty() ? null : new JSONTokener(data).nextValue();
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
		try(InputStream in = HttpRequest.get(url).userAgent("HammerCore/@VERSION@").stream())
		{
			return ImageIO.read(in);
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
				internet = HttpRequest.get(url).userAgent("HammerLib " + ModHelper.getModVersion(HLConstants.MODID) + "; Minecraft 1.12.2").stream();
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
			byte[] buf = new byte[IOUtils.heapLimit];
			int read;
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
			DeflaterOutputStream o = new DeflaterOutputStream(baos, new Deflater(9));
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
	 * @param url
	 *            The source link
	 * @return The target link
	 * @throws IOException
	 *             if failed to connect to the source link
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
	
	/**
	 * Picks the file that doesn't exist by appending "(n)" at the end of the
	 * name
	 * 
	 * @param f
	 *            The source file
	 * @return The target file
	 */
	public static File pickFile(File f)
	{
		if(f.isFile())
		{
			String ext = f.getName().contains(".") ? f.getName().substring(f.getName().lastIndexOf(".")) : "";
			String name = f.getName().contains(".") ? f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")) : f.getAbsolutePath();
			
			int i = 1;
			while(true)
			{
				File nf = new File(name + " (" + i + ")" + ext);
				if(!nf.isFile())
					return nf;
				
				++i;
			}
		} else if(f.isDirectory())
		{
			String name = f.getAbsolutePath();
			
			int i = 1;
			while(true)
			{
				File nf = new File(name + " (" + i + ")");
				if(!nf.isDirectory())
					return nf;
				
				++i;
			}
		}
		
		return f;
	}
}