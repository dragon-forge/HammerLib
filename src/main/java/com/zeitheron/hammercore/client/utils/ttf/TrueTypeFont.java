package com.zeitheron.hammercore.client.utils.ttf;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.zeitheron.hammercore.utils.WrappedLog;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class TrueTypeFont
{
	public static final WrappedLog logger = new WrappedLog("TrueTypeFontLoader");
	
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;
	private FloatObject[] charArray = new FloatObject[256];
	private Map<Character, FloatObject> customChars = new HashMap<>();
	protected boolean antiAlias;
	private float fontSize = 0.0f;
	private float fontHeight = 0.0f;
	private int fontTextureID;
	private int textureWidth = 1024;
	private int textureHeight = 1024;
	protected Font font;
	private FontMetrics fontMetrics;
	private int correctL = 9;
	private int correctR = 8;
	
	public TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars)
	{
		this.font = font;
		this.antiAlias = antiAlias;
		fontSize = font.getSize() + 3;
		createSet(additionalChars);
		logger.info("TrueTypeFont loaded: " + font + " - AntiAlias = " + antiAlias);
		fontHeight -= 1F;
		if(fontHeight <= 0F)
			fontHeight = 1F;
	}
	
	public TrueTypeFont(Font font, boolean antiAlias)
	{
		this(font, antiAlias, null);
	}
	
	public void setCorrection(boolean on)
	{
		if(on)
		{
			this.correctL = 2;
			this.correctR = 1;
		} else
		{
			this.correctL = 0;
			this.correctR = 0;
		}
	}
	
	private BufferedImage getFontImage(char ch)
	{
		float charheight;
		BufferedImage tempfontImage = new BufferedImage(1, 1, 2);
		Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
		if(antiAlias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(this.font);
		fontMetrics = g.getFontMetrics();
		float charwidth = fontMetrics.charWidth(ch) + 8;
		if(charwidth <= 0F)
			charwidth = 7F;
		if((charheight = fontMetrics.getHeight() + 3) <= 0F)
			charheight = fontSize;
		BufferedImage fontImage = new BufferedImage((int) charwidth, (int) charheight, 2);
		Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if(antiAlias)
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gt.setFont(font);
		gt.setColor(Color.WHITE);
		int charx = 3;
		int chary = 1;
		gt.drawString(String.valueOf(ch), charx, chary + fontMetrics.getAscent());
		return fontImage;
	}
	
	private void createSet(char[] customCharsArray)
	{
		if(customCharsArray != null && customCharsArray.length > 0)
			textureWidth *= 2;
		try
		{
			BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, 2);
			Graphics2D g = imgTemp.createGraphics();
			g.setColor(new Color(0, 0, 0, 1));
			g.fillRect(0, 0, textureWidth, textureHeight);
			float rowHeight = 0;
			float positionX = 0;
			float positionY = 0;
			int customCharsLength = customCharsArray != null ? customCharsArray.length : 0;
			for(int i = 0; i < 256 + customCharsLength; ++i)
			{
				char ch = i < 256 ? (char) i : customCharsArray[i - 256];
				BufferedImage fontImage = getFontImage(ch);
				FloatObject newIntObject = new FloatObject();
				newIntObject.width = fontImage.getWidth();
				newIntObject.height = fontImage.getHeight();
				if(positionX + newIntObject.width >= textureWidth)
				{
					positionX = 0.0f;
					positionY += rowHeight;
					rowHeight = 0.0f;
				}
				newIntObject.storedX = positionX;
				newIntObject.storedY = positionY;
				if(newIntObject.height > fontHeight)
					fontHeight = newIntObject.height;
				if(newIntObject.height > rowHeight)
					rowHeight = newIntObject.height;
				g.drawImage(fontImage, (int) positionX, (int) positionY, null);
				positionX += newIntObject.width;
				if(i < 256)
					charArray[i] = newIntObject;
				else
					customChars.put(ch, newIntObject);
				fontImage = null;
			}
			this.fontTextureID = TrueTypeFont.loadImage(imgTemp);
		} catch(Exception e)
		{
			System.err.println("Failed to create font.");
			e.printStackTrace();
		}
	}
	
	private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2, float[] rgba)
	{
		float DrawWidth = drawX2 - drawX;
		float DrawHeight = drawY2 - drawY;
		float TextureSrcX = srcX / textureWidth;
		float TextureSrcY = srcY / textureHeight;
		float SrcWidth = srcX2 - srcX;
		float SrcHeight = srcY2 - srcY;
		float RenderWidth = SrcWidth / textureWidth;
		float RenderHeight = SrcHeight / textureHeight;
		BufferBuilder t = Tessellator.getInstance().getBuffer();
		t.pos(drawX, drawY, 0.0).tex(TextureSrcX, TextureSrcY).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
		t.pos(drawX, drawY + DrawHeight, 0.0).tex(TextureSrcX, TextureSrcY + RenderHeight).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
		t.pos(drawX + DrawWidth, drawY + DrawHeight, 0.0).tex(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
		t.pos(drawX + DrawWidth, drawY, 0.0).tex(TextureSrcX + RenderWidth, TextureSrcY).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
	}
	
	public float getWidth(String whatchars)
	{
		float totalwidth = 0.0f;
		FloatObject floatObject = null;
		char currentChar = '\u0000';
		float lastWidth = -10.0f;
		for(int i = 0; i < whatchars.length(); ++i)
		{
			currentChar = whatchars.charAt(i);
			floatObject = currentChar < '\u0100' ? charArray[currentChar] : customChars.get(currentChar);
			if(floatObject == null)
				continue;
			totalwidth += floatObject.width / 2F;
			lastWidth = floatObject.width;
		}
		return fontMetrics.stringWidth(whatchars);
	}
	
	public float getHeight()
	{
		return fontHeight;
	}
	
	public float getHeight(String HeightString)
	{
		return fontHeight;
	}
	
	public float getLineHeight()
	{
		return fontHeight;
	}
	
	public void drawString(float x, float y, String whatchars, float scaleX, float scaleY, float... rgba)
	{
		if(rgba.length == 0)
			rgba = new float[] { 1, 1, 1, 1 };
		if(rgba.length == 3)
			rgba = new float[] { rgba[0], rgba[1], rgba[2], 1F };
		if(rgba.length == 2)
			rgba = new float[] { 0F, rgba[0], rgba[1], 1F };
		if(rgba.length == 1)
			rgba = new float[] { 0F, 0F, rgba[0], 1F };
		drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, 0, rgba);
	}
	
	public void drawString(float x, float y, String whatchars, float scaleX, float scaleY, int format, float... rgba)
	{
		if(rgba.length == 0)
			rgba = new float[] { 1, 1, 1, 1 };
		if(rgba.length == 3)
			rgba = new float[] { rgba[0], rgba[1], rgba[2], 1F };
		if(rgba.length == 2)
			rgba = new float[] { 0F, rgba[0], rgba[1], 1F };
		if(rgba.length == 1)
			rgba = new float[] { 0F, 0F, rgba[0], 1F };
		drawString(x, y, whatchars, 0, whatchars.length() - 1, scaleX, scaleY, format, rgba);
	}
	
	public void drawString(float x, float y, String whatchars, int startIndex, int endIndex, float scaleX, float scaleY, int format, float... rgba)
	{
		int c;
		int d;
		char charCurrent;
		int i;
		if(rgba.length == 0)
			rgba = new float[] { 1, 1, 1, 1 };
		if(rgba.length == 3)
			rgba = new float[] { rgba[0], rgba[1], rgba[2], 1F };
		if(rgba.length == 2)
			rgba = new float[] { 0F, rgba[0], rgba[1], 1F };
		if(rgba.length == 1)
			rgba = new float[] { 0F, 0F, rgba[0], 1F };
		GL11.glPushMatrix();
		GL11.glScalef(scaleX, scaleY, 1);
		FloatObject floatObject = null;
		float totalwidth = 0.0f;
		float startY = 0.0f;
		switch(format)
		{
		case 1:
		{
			d = -1;
			c = correctR;
			for(i = startIndex; i < endIndex; ++i)
			{
				if(whatchars.charAt(i) != '\n')
					continue;
				startY -= fontHeight;
			}
			break;
		}
		case 2:
		{
			for(int l = startIndex; l <= endIndex && (charCurrent = whatchars.charAt(l)) != '\n'; ++l)
			{
				floatObject = charCurrent < '\u0100' ? charArray[charCurrent] : customChars.get(charCurrent);
				totalwidth += floatObject.width - correctL;
			}
			totalwidth /= -2.0f;
		}
		default:
		{
			d = 1;
			c = this.correctL;
		}
		}
		
		i = startIndex;
		
		GL11.glBindTexture(3553, this.fontTextureID);
		Tessellator t = Tessellator.getInstance();
		t.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		while(i >= startIndex && i <= endIndex)
		{
			charCurrent = whatchars.charAt(i);
			floatObject = charCurrent < '\u0100' ? charArray[charCurrent] : customChars.get(charCurrent);
			if(floatObject == null)
				continue;
			if(d < 0)
				totalwidth += (floatObject.width - c) * d;
			if(charCurrent == '\n')
			{
				startY -= this.fontHeight * d;
				totalwidth = 0.0f;
				if(format == 2)
				{
					for(int l = i + 1; l <= endIndex && (charCurrent = whatchars.charAt(l)) != '\n'; ++l)
					{
						floatObject = charCurrent < '\u0100' ? charArray[charCurrent] : customChars.get(charCurrent);
						totalwidth += floatObject.width - correctL;
					}
					totalwidth /= -2.0f;
				}
			} else
			{
				drawQuad(totalwidth + floatObject.width + x / scaleX, startY + y / scaleY, totalwidth + x / scaleX, startY + floatObject.height + y / scaleY, floatObject.storedX + floatObject.width, floatObject.storedY + floatObject.height, floatObject.storedX, floatObject.storedY, rgba);
				if(d > 0)
					totalwidth += (floatObject.width - c) * d;
			}
			i += d;
		}
		t.draw();
		GL11.glPopMatrix();
	}
	
	public static int loadImage(BufferedImage bufferedImage)
	{
		try
		{
			ByteBuffer byteBuffer;
			short width = (short) bufferedImage.getWidth();
			short height = (short) bufferedImage.getHeight();
			byte bpp = (byte) bufferedImage.getColorModel().getPixelSize();
			DataBuffer db = bufferedImage.getData().getDataBuffer();
			if(db instanceof DataBufferInt)
			{
				int[] intI = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();
				byte[] newI = new byte[intI.length * 4];
				for(int i = 0; i < intI.length; ++i)
				{
					byte[] b = TrueTypeFont.intToByteArray(intI[i]);
					int newIndex = i * 4;
					newI[newIndex] = b[1];
					newI[newIndex + 1] = b[2];
					newI[newIndex + 2] = b[3];
					newI[newIndex + 3] = b[0];
				}
				byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(newI);
			} else
			{
				byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8)).order(ByteOrder.nativeOrder()).put(((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData());
			}
			byteBuffer.flip();
			int internalFormat = 32856;
			int format = 6408;
			IntBuffer textureId = BufferUtils.createIntBuffer(1);
			GL11.glGenTextures(textureId);
			GL11.glBindTexture(3553, textureId.get(0));
			GL11.glTexParameteri(3553, 10242, 10496);
			GL11.glTexParameteri(3553, 10243, 10496);
			GL11.glTexParameteri(3553, 10240, 9728);
			GL11.glTexParameteri(3553, 10241, 9728);
			GL11.glTexEnvf(8960, 8704, 8448);
			GLU.gluBuild2DMipmaps(3553, internalFormat, width, height, format, 5121, byteBuffer);
			return textureId.get(0);
		} catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
			return -1;
		}
	}
	
	public static boolean isSupported(String fontname)
	{
		Font[] font = TrueTypeFont.getFonts();
		for(int i = font.length - 1; i >= 0; --i)
		{
			if(!font[i].getName().equalsIgnoreCase(fontname))
				continue;
			return true;
		}
		return false;
	}
	
	public static Font[] getFonts()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
	}
	
	public static byte[] intToByteArray(int value)
	{
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}
	
	public void destroy()
	{
		IntBuffer scratch = BufferUtils.createIntBuffer(1);
		scratch.put(0, fontTextureID);
		GL11.glBindTexture(3553, 0);
		GL11.glDeleteTextures(scratch);
	}
	
	private class FloatObject
	{
		public float width;
		public float height;
		public float storedX;
		public float storedY;
		
		private FloatObject()
		{
		}
	}
}
