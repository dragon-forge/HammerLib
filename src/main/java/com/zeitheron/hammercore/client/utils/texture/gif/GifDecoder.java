package com.zeitheron.hammercore.client.utils.texture.gif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zeitheron.hammercore.client.utils.IImagePreprocessor;
import com.zeitheron.hammercore.client.utils.texture.TexLocUploader;
import com.zeitheron.hammercore.lib.zlib.tuple.TwoTuple;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GifDecoder
{
	public static GLGifInfo decodeGifIntoVideoMem(File gif, IImagePreprocessor... proc) throws IOException
	{
		return decodeGifIntoVideoMem(gif.toURI().toURL(), proc);
	}
	
	public static GLGifInfo decodeGifIntoVideoMem(URL gif, IImagePreprocessor... proc) throws IOException
	{
		return decodeGifIntoVideoMem(gif.openStream(), proc);
	}
	
	public static GLGifInfo decodeGifIntoVideoMem(InputStream gif, IImagePreprocessor... proc) throws IOException
	{
		TwoTuple<ImageFrame[], int[]> info = readGIF(gif);
		ImageFrame[] frames = info.get1();
		ResourceLocation[] gl = new ResourceLocation[frames.length];
		for(int i = 0; i < gl.length; ++i)
		{
			final int fid = i;
			ResourceLocation rl = new ResourceLocation("hammercore", "gifs/" + Integer.toHexString(gif.hashCode()) + "-" + i + ".png");
			
			BufferedImage cap = frames[fid].getImage();
			for(IImagePreprocessor p  : proc)
				cap = p.process(cap);
			BufferedImage fimg = cap;
			
			Minecraft.getMinecraft().addScheduledTask(() -> TexLocUploader.upload(rl, fimg));
			TexLocUploader.cleanupAfterLogoff(rl);
			
			gl[i] = rl;
		}
		return new GLGifInfo(gl, frames[0].getImage().getWidth(), frames[0].getImage().getHeight(), info.get2());
	}
	
	private static TwoTuple<ImageFrame[], int[]> readGIF(InputStream in) throws IOException
	{
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		ImageInputStream stream = ImageIO.createImageInputStream(in);
		reader.setInput(stream);
		
		ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);
		IntList delays = new IntArrayList(2);
		
		int width = -1;
		int height = -1;
		
		IIOMetadata metadata = reader.getStreamMetadata();
		if(metadata != null)
		{
			IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
			
			NodeList globalScreenDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");
			
			if(globalScreenDescriptor != null && globalScreenDescriptor.getLength() > 0)
			{
				IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor.item(0);
				
				if(screenDescriptor != null)
				{
					width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
					height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
				}
			}
		}
		
		BufferedImage master = null;
		Graphics2D masterGraphics = null;
		
		for(int frameIndex = 0;; frameIndex++)
		{
			BufferedImage image;
			try
			{
				image = reader.read(frameIndex);
			} catch(IndexOutOfBoundsException io)
			{
				break;
			}
			
			if(width == -1 || height == -1)
			{
				width = image.getWidth();
				height = image.getHeight();
			}
			
			IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
			IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
			int delay = 1;
			try
			{
				delay = Integer.parseInt(gce.getAttribute("delayTime"));
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
			String disposal = gce.getAttribute("disposalMethod");
			
			int x = 0;
			int y = 0;
			
			if(master == null)
			{
				master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				masterGraphics = master.createGraphics();
				masterGraphics.setBackground(new Color(0, 0, 0, 0));
			} else
			{
				NodeList children = root.getChildNodes();
				for(int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++)
				{
					Node nodeItem = children.item(nodeIndex);
					if(nodeItem.getNodeName().equals("ImageDescriptor"))
					{
						NamedNodeMap map = nodeItem.getAttributes();
						x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
						y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
					}
				}
			}
			masterGraphics.drawImage(image, x, y, null);
			
			BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
			frames.add(new ImageFrame(copy, disposal));
			delays.add(delay);
			
			if(disposal.equals("restoreToPrevious"))
			{
				BufferedImage from = null;
				for(int i = frameIndex - 1; i >= 0; i--)
				{
					if(!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0)
					{
						from = frames.get(i).getImage();
						break;
					}
				}
				
				master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
				masterGraphics = master.createGraphics();
				masterGraphics.setBackground(new Color(0, 0, 0, 0));
			} else if(disposal.equals("restoreToBackgroundColor"))
			{
				masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
			}
		}
		reader.dispose();
		
		return new TwoTuple<>(frames.toArray(new ImageFrame[frames.size()]), delays.toIntArray());
	}
	
	private static class ImageFrame
	{
		private final BufferedImage image;
		private final String disposal;
		
		public ImageFrame(BufferedImage image, String disposal)
		{
			this.image = image;
			this.disposal = disposal;
		}
		
		public BufferedImage getImage()
		{
			return image;
		}
		
		public String getDisposal()
		{
			return disposal;
		}
	}
}