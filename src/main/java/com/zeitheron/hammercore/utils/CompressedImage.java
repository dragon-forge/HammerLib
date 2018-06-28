package com.zeitheron.hammercore.utils;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class CompressedImage
{
	private static final byte[] endBuffer = new byte[] { (byte) 0xAE, (byte) 0x42, (byte) 0x60, (byte) 0x82 };
	private BufferedImage bufImage;
	
	public CompressedImage(int width, int height)
	{
		bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public int getRGB(int x, int y)
	{
		return bufImage.getRGB(x, y);
	}
	
	public void setRGB(int x, int y, int rgb)
	{
		bufImage.setRGB(x, y, rgb);
	}
	
	public int getWidth()
	{
		return bufImage.getWidth();
	}
	
	public int getHeight()
	{
		return bufImage.getHeight();
	}
	
	public BufferedImage toBufferedImage()
	{
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		img.createGraphics().drawImage(bufImage, 0, 0, getWidth(), getHeight(), null);
		return img;
	}
	
	public static CompressedImage wrap(BufferedImage img)
	{
		CompressedImage i = new CompressedImage(img.getWidth(), img.getHeight());
		for(int x = 0; x < i.getWidth(); ++x)
			for(int y = 0; y < i.getHeight(); ++y)
				i.setRGB(x, y, img.getRGB(x, y));
		return i;
	}
	
	public void write(OutputStream output) throws IOException
	{
		output.write(0x89);
		output.write("CIG".getBytes());
		DataOutputStream data = new DataOutputStream(output);
		int w = bufImage.getWidth();
		int h = bufImage.getHeight();
		data.writeInt(w);
		data.writeInt(h);
		for(int x = 0; x < w; ++x)
			for(int y = 0; y < h; ++y)
				data.writeInt(getRGB(x, y));
		output.write("IEND".getBytes());
		output.write(endBuffer);
	}
	
	public static CompressedImage read(InputStream input) throws IOException
	{
		if(input.read() != 0x89)
			throw new IOException("Unexpected beginning char at 0!");
		byte[] b = new byte[3];
		input.read(b);
		if(!new String(b).equals("CIG"))
			throw new IOException("Unexpected beginning chars at 1-3!");
		
		DataInputStream data = new DataInputStream(input);
		
		int width = data.readInt();
		int height = data.readInt();
		CompressedImage image = new CompressedImage(width, height);
		for(int x = 0; x < width; ++x)
			for(int y = 0; y < height; ++y)
				image.setRGB(x, y, data.readInt());
			
		b = new byte[4];
		input.read(b);
		if(!new String(b).equals("IEND"))
			throw new IOException("Unexpected end!");
		input.read(b);
		if(!Arrays.equals(endBuffer, b))
			throw new IOException("Unexpected end!");
		return image;
	}
}