package com.zeitheron.hammercore.lib.zlib.image.cig;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.json.JSONArray;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.json.JSONTokener;
import com.zeitheron.hammercore.lib.zlib.json.serapi.Jsonable;

public class CompressedImage implements Jsonable
{
	private static final byte[] endBuffer = new byte[] { -82, 66, 96, -126 };
	private BufferedImage bufImage;
	
	public CompressedImage(int width, int height)
	{
		this.bufImage = new BufferedImage(width, height, 2);
	}
	
	public CompressedImage(String json) throws JSONException
	{
		JSONObject obj = (JSONObject) new JSONTokener(json).nextValue();
		this.bufImage = new BufferedImage(obj.getInt("width"), obj.getInt("height"), 2);
		JSONArray pixels = obj.getJSONArray("pixels");
		int elem = 0;
		int x = 0;
		while(x < this.getWidth())
		{
			int y = 0;
			while(y < this.getHeight())
			{
				this.bufImage.setRGB(x, y, pixels.getInt(elem));
				++elem;
				++y;
			}
			++x;
		}
	}
	
	public int getRGB(int x, int y)
	{
		return this.bufImage.getRGB(x, y);
	}
	
	public void setRGB(int x, int y, int rgb)
	{
		this.bufImage.setRGB(x, y, rgb);
	}
	
	public int getWidth()
	{
		return this.bufImage.getWidth();
	}
	
	public int getHeight()
	{
		return this.bufImage.getHeight();
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), 2);
		img.createGraphics().drawImage(this.bufImage, 0, 0, this.getWidth(), this.getHeight(), null);
		return img;
	}
	
	public static CompressedImage wrap(BufferedImage img)
	{
		CompressedImage i = new CompressedImage(img.getWidth(), img.getHeight());
		int x = 0;
		while(x < i.getWidth())
		{
			int y = 0;
			while(y < i.getHeight())
			{
				i.setRGB(x, y, img.getRGB(x, y));
				++y;
			}
			++x;
		}
		return i;
	}
	
	public void write(OutputStream output) throws IOException
	{
		output.write(137);
		output.write("CIG".getBytes());
		DataOutputStream data = new DataOutputStream(output);
		int w = this.bufImage.getWidth();
		int h = this.bufImage.getHeight();
		data.writeInt(w);
		data.writeInt(h);
		int x = 0;
		while(x < w)
		{
			int y = 0;
			while(y < h)
			{
				data.writeInt(this.getRGB(x, y));
				++y;
			}
			++x;
		}
		output.write("IEND".getBytes());
		output.write(endBuffer);
	}
	
	public static CompressedImage read(InputStream input) throws IOException
	{
		if(input.read() != 137)
		{
			throw new IOException("Unexpected beginning char at 0!");
		}
		byte[] b = new byte[3];
		input.read(b);
		if(!new String(b).equals("CIG"))
		{
			throw new IOException("Unexpected beginning chars at 1-3!");
		}
		DataInputStream data = new DataInputStream(input);
		int width = data.readInt();
		int height = data.readInt();
		CompressedImage image = new CompressedImage(width, height);
		int x = 0;
		while(x < width)
		{
			int y = 0;
			while(y < height)
			{
				image.setRGB(x, y, data.readInt());
				++y;
			}
			++x;
		}
		b = new byte[4];
		input.read(b);
		if(!new String(b).equals("IEND"))
		{
			throw new IOException("Unexpected end!");
		}
		input.read(b);
		if(!Arrays.equals(endBuffer, b))
		{
			throw new IOException("Unexpected end!");
		}
		return image;
	}
	
	@Override
	public String serialize()
	{
		String s = "{\"width\":" + this.getWidth() + ",\"height\":" + this.getHeight() + ",";
		String px = "";
		int x = 0;
		while(x < this.getWidth())
		{
			int y = 0;
			while(y < this.getHeight())
			{
				px = String.valueOf(px) + "0x" + Integer.toHexString(this.getRGB(x, y)).toUpperCase() + ",";
				++y;
			}
			++x;
		}
		if(px.endsWith(","))
		{
			px = px.substring(0, px.length() - 1);
		}
		return String.valueOf(s) + "\"pixels\":[" + px + "]}";
	}
}
