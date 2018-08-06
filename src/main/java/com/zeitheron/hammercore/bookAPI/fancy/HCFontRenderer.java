package com.zeitheron.hammercore.bookAPI.fancy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.zeitheron.hammercore.client.utils.UtilsFX;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HCFontRenderer
{
	private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
	public static final ResourceLocation FONT_NORMAL = new ResourceLocation("textures/font/ascii.png");
	public static final ResourceLocation FONT_GALACTIC = new ResourceLocation("textures/font/ascii_sga.png");
	private int[] charWidth = new int[256];
	public int FONT_HEIGHT = 9;
	public Random fontRandom = new Random();
	private byte[] glyphWidth = new byte[65536];
	private int[] colorCode = new int[32];
	private ResourceLocation locationFontTexture;
	private final TextureManager renderEngine;
	private float posX;
	private float posY;
	private boolean unicodeFlag;
	private boolean bidiFlag;
	private float red;
	private float blue;
	private float green;
	private float alpha;
	private int textColor;
	private boolean randomStyle = false;
	private boolean boldStyle = false;
	private boolean italicStyle = false;
	private boolean underlineStyle = false;
	private boolean strikethroughStyle = false;
	boolean uniflagInit = false;
	ArrayList<String> inserts = new ArrayList();
	
	public HCFontRenderer(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3RenderEngine, boolean par4)
	{
		this.locationFontTexture = par2ResourceLocation;
		this.renderEngine = par3RenderEngine;
		this.unicodeFlag = par4;
		this.uniflagInit = par4;
		this.readFontData();
		par3RenderEngine.bindTexture(this.locationFontTexture);
		for(int i = 0; i < 32; ++i)
		{
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;
			if(i == 6)
			{
				k += 85;
			}
			if(par1GameSettings.anaglyph)
			{
				int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
				int k1 = (k * 30 + l * 70) / 100;
				int l1 = (k * 30 + i1 * 70) / 100;
				k = j1;
				l = k1;
				i1 = l1;
			}
			if(i >= 16)
			{
				k /= 4;
				l /= 4;
				i1 /= 4;
			}
			this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}
	}
	
	public void readFontData()
	{
		this.readGlyphSizes();
		this.readFontTexture();
	}
	
	private void readFontTexture()
	{
		BufferedImage bufferedimage;
		try
		{
			bufferedimage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
		} catch(IOException ioexception)
		{
			throw new RuntimeException(ioexception);
		}
		int i = bufferedimage.getWidth();
		int j = bufferedimage.getHeight();
		int[] aint = new int[i * j];
		bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
		for(int k = 0; k < 256; ++k)
		{
			int j1;
			int l = k % 16;
			int i1 = k / 16;
			for(j1 = 7; j1 >= 0; --j1)
			{
				int k1 = l * 8 + j1;
				boolean flag = true;
				for(int l1 = 0; l1 < 8 && flag; ++l1)
				{
					int i2 = (i1 * 8 + l1) * i;
					int j2 = aint[k1 + i2] & 255;
					if(j2 <= 0)
						continue;
					flag = false;
				}
				if(!flag)
					break;
			}
			if(k == 32)
			{
				j1 = 2;
			}
			this.charWidth[k] = j1 + 2;
		}
	}
	
	private void readGlyphSizes()
	{
		try
		{
			InputStream inputstream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
			inputstream.read(this.glyphWidth);
		} catch(IOException ioexception)
		{
			throw new RuntimeException(ioexception);
		}
	}
	
	private float renderCharAtPos(int par1, char par2, boolean par3)
	{
		return par2 == ' ' ? 4.0f : (par1 > 0 && !this.unicodeFlag ? this.renderDefaultChar(par1 + 32, par3) : this.renderUnicodeChar(par2, par3));
	}
	
	private float renderDefaultChar(int par1, boolean par2)
	{
		float f = par1 % 16 * 8;
		float f1 = par1 / 16 * 8;
		float f2 = par2 ? 1.0f : 0.0f;
		this.renderEngine.bindTexture(this.locationFontTexture);
		float f3 = this.charWidth[par1] - 0.01f;
		GL11.glBegin(5);
		GL11.glTexCoord2f(f / 128.0f, f1 / 128.0f);
		GL11.glVertex3f(this.posX + f2, this.posY, 0.0f);
		GL11.glTexCoord2f(f / 128.0f, (f1 + 7.99f) / 128.0f);
		GL11.glVertex3f(this.posX - f2, this.posY + 7.99f, 0.0f);
		GL11.glTexCoord2f((f + f3) / 128.0f, f1 / 128.0f);
		GL11.glVertex3f(this.posX + f3 + f2, this.posY, 0.0f);
		GL11.glTexCoord2f((f + f3) / 128.0f, (f1 + 7.99f) / 128.0f);
		GL11.glVertex3f(this.posX + f3 - f2, this.posY + 7.99f, 0.0f);
		GL11.glEnd();
		return this.charWidth[par1];
	}
	
	private ResourceLocation getUnicodePageLocation(int par1)
	{
		if(unicodePageLocations[par1] == null)
			HCFontRenderer.unicodePageLocations[par1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", par1));
		return unicodePageLocations[par1];
	}
	
	private void loadGlyphTexture(int par1)
	{
		this.renderEngine.bindTexture(this.getUnicodePageLocation(par1));
	}
	
	private float renderUnicodeChar(char par1, boolean par2)
	{
		if(this.glyphWidth[par1] == 0)
			return 0.0f;
		int i = par1 / 256;
		this.loadGlyphTexture(i);
		int j = this.glyphWidth[par1] >>> 4;
		int k = this.glyphWidth[par1] & 15;
		float f = j;
		float f1 = k + 1;
		float f2 = par1 % 16 * 16 + f;
		float f3 = (par1 & 255) / 16 * 16;
		float f4 = f1 - f - 0.02f;
		float f5 = par2 ? 1.0f : 0.0f;
		GL11.glBegin(5);
		GL11.glTexCoord2f(f2 / 256.0f, f3 / 256.0f);
		GL11.glVertex3f(this.posX + f5, this.posY, 0.0f);
		GL11.glTexCoord2f(f2 / 256.0f, (f3 + 15.98f) / 256.0f);
		GL11.glVertex3f(this.posX - f5, this.posY + 7.99f, 0.0f);
		GL11.glTexCoord2f((f2 + f4) / 256.0f, f3 / 256.0f);
		GL11.glVertex3f(this.posX + f4 / 2.0f + f5, this.posY, 0.0f);
		GL11.glTexCoord2f((f2 + f4) / 256.0f, (f3 + 15.98f) / 256.0f);
		GL11.glVertex3f(this.posX + f4 / 2.0f - f5, this.posY + 7.99f, 0.0f);
		GL11.glEnd();
		return (f1 - f) / 2.0f + 1.0f;
	}
	
	public int drawStringWithShadow(String par1Str, int par2, int par3, int par4)
	{
		return this.drawString(par1Str, par2, par3, par4, true);
	}
	
	public int drawString(String par1Str, int par2, int par3, int par4)
	{
		return this.drawString(par1Str, par2, par3, par4, false);
	}
	
	public int drawString(String par1Str, int par2, int par3, int par4, boolean par5)
	{
		int l;
		this.resetStyles();
		if(this.bidiFlag)
			par1Str = this.bidiReorder(par1Str);
		if(par5)
		{
			l = this.renderString(par1Str, par2 + 1, par3 + 1, par4, true);
			l = Math.max(l, this.renderString(par1Str, par2, par3, par4, false));
		} else
			l = this.renderString(par1Str, par2, par3, par4, false);
		return l;
	}
	
	private String bidiReorder(String par1Str)
	{
		if(par1Str != null && Bidi.requiresBidi(par1Str.toCharArray(), 0, par1Str.length()))
		{
			int i;
			Bidi bidi = new Bidi(par1Str, -2);
			byte[] abyte = new byte[bidi.getRunCount()];
			Object[] astring = new String[abyte.length];
			for(int j = 0; j < abyte.length; ++j)
			{
				int k = bidi.getRunStart(j);
				i = bidi.getRunLimit(j);
				int l = bidi.getRunLevel(j);
				String s1 = par1Str.substring(k, i);
				abyte[j] = (byte) l;
				astring[j] = s1;
			}
			String[] astring1 = (String[]) astring.clone();
			Bidi.reorderVisually(abyte, 0, astring, 0, abyte.length);
			StringBuilder stringbuilder = new StringBuilder();
			for(i = 0; i < astring.length; ++i)
			{
				int i1;
				byte b0 = abyte[i];
				for(i1 = 0; i1 < astring1.length; ++i1)
				{
					if(!astring1[i1].equals(astring[i]))
					{
						continue;
					}
					b0 = abyte[i1];
					break;
				}
				if((b0 & 1) == 0)
				{
					stringbuilder.append((String) astring[i]);
					continue;
				}
				for(i1 = astring[i].toString().length() - 1; i1 >= 0; --i1)
				{
					char c0 = astring[i].toString().charAt(i1);
					if(c0 == '(')
					{
						c0 = ')';
					} else if(c0 == ')')
					{
						c0 = '(';
					}
					stringbuilder.append(c0);
				}
			}
			return stringbuilder.toString();
		}
		return par1Str;
	}
	
	private void resetStyles()
	{
		this.randomStyle = false;
		this.boldStyle = false;
		this.italicStyle = false;
		this.underlineStyle = false;
		this.strikethroughStyle = false;
		red = 1;
		green = 1;
		blue = 1;
	}
	
	private void renderStringAtPos(String par1Str, boolean par2)
	{
		for(int i = 0; i < par1Str.length(); ++i)
		{
			boolean flag1;
			int j;
			int k;
			Tessellator tessellator;
			char c0 = par1Str.charAt(i);
			if(c0 == '\u00a7' && i + 1 < par1Str.length())
			{
				j = "0123456789abcdefklmnor".indexOf(par1Str.toLowerCase().charAt(i + 1));
				if(j < 16)
				{
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					if(j < 0 || j > 15)
					{
						j = 15;
					}
					if(par2)
					{
						j += 16;
					}
					this.textColor = k = this.colorCode[j];
					GL11.glColor4f((k >> 16) / 255.0f, (k >> 8 & 255) / 255.0f, (k & 255) / 255.0f, this.alpha);
				} else if(j == 16)
				{
					this.randomStyle = true;
				} else if(j == 17)
				{
					this.boldStyle = true;
				} else if(j == 18)
				{
					this.strikethroughStyle = true;
				} else if(j == 19)
				{
					this.underlineStyle = true;
				} else if(j == 20)
				{
					this.italicStyle = true;
				} else if(j == 21)
				{
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
				}
				++i;
				continue;
			}
			j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);
			if(this.randomStyle && j != -1)
			{
				while(this.charWidth[j] != this.charWidth[k = this.fontRandom.nextInt(this.charWidth.length)])
				{
				}
				j = k;
			}
			float f = this.unicodeFlag ? 0.5f : 1.0f;
			boolean bl = flag1 = (j <= 0 || this.unicodeFlag) && par2;
			if(flag1)
			{
				this.posX -= f;
				this.posY -= f;
			}
			
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			
			float f1 = this.renderCharAtPos(j, c0, this.italicStyle);
			if(flag1)
			{
				this.posX += f;
				this.posY += f;
			}
			if(this.boldStyle)
			{
				this.posX += f;
				if(flag1)
				{
					this.posX -= f;
					this.posY -= f;
				}
				this.renderCharAtPos(j, c0, this.italicStyle);
				this.posX -= f;
				if(flag1)
				{
					this.posX += f;
					this.posY += f;
				}
				f1 += 1.0f;
			}
			if(this.strikethroughStyle)
			{
				tessellator = Tessellator.getInstance();
				GL11.glDisable(3553);
				BufferBuilder b = tessellator.getBuffer();
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
				b.pos(this.posX, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
				b.pos(this.posX + f1, this.posY + this.FONT_HEIGHT / 2, 0.0).endVertex();
				b.pos(this.posX + f1, this.posY + this.FONT_HEIGHT / 2 - 1.0f, 0.0).endVertex();
				b.pos(this.posX, this.posY + this.FONT_HEIGHT / 2 - 1.0f, 0.0).endVertex();
				tessellator.draw();
				GL11.glEnable(3553);
			}
			if(this.underlineStyle)
			{
				tessellator = Tessellator.getInstance();
				GL11.glDisable(3553);
				BufferBuilder b = tessellator.getBuffer();
				b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
				int l = this.underlineStyle ? -1 : 0;
				b.pos(this.posX + l, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
				b.pos(this.posX + f1, this.posY + this.FONT_HEIGHT, 0.0).endVertex();
				b.pos(this.posX + f1, this.posY + this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
				b.pos(this.posX + l, this.posY + this.FONT_HEIGHT - 1.0f, 0.0).endVertex();
				tessellator.draw();
				GL11.glEnable(3553);
			}
			this.posX += ((int) f1);
		}
	}
	
	private int renderStringAligned(String par1Str, int par2, int par3, int par4, int par5, boolean par6)
	{
		if(this.bidiFlag)
		{
			par1Str = this.bidiReorder(par1Str);
			int i1 = this.getStringWidth(par1Str);
			par2 = par2 + par4 - i1;
		}
		return this.renderString(par1Str, par2, par3, par5, par6);
	}
	
	private int renderString(String par1Str, int par2, int par3, int par4, boolean par5)
	{
		if(par1Str == null)
			return 0;
		if((par4 & -67108864) == 0)
			par4 |= -16777216;
		if(par5)
			par4 = (par4 & 16579836) >> 2 | par4 & -16777216;
		this.red = (par4 >> 16 & 255) / 255.0f;
		this.blue = (par4 >> 8 & 255) / 255.0f;
		this.green = (par4 & 255) / 255.0f;
		this.alpha = (par4 >> 24 & 255) / 255.0f;
		GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
		this.posX = par2;
		this.posY = par3;
		this.renderStringAtPos(par1Str, par5);
		return (int) this.posX;
	}
	
	public int getStringWidth(String par1Str)
	{
		if(par1Str == null)
		{
			return 0;
		}
		int i = 0;
		boolean flag = false;
		for(int j = 0; j < par1Str.length(); ++j)
		{
			char c0 = par1Str.charAt(j);
			int k = this.getCharWidth(c0);
			if(k < 0 && j < par1Str.length() - 1)
			{
				if((c0 = par1Str.charAt(++j)) != 'l' && c0 != 'L')
				{
					if(c0 == 'r' || c0 == 'R')
					{
						flag = false;
					}
				} else
				{
					flag = true;
				}
				k = 0;
			}
			i += k;
			if(!flag)
				continue;
			++i;
		}
		return i;
	}
	
	public int getCharWidth(char par1)
	{
		if(par1 == '\u00a7')
		{
			return -1;
		}
		if(par1 == ' ')
		{
			return 4;
		}
		int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(par1);
		if(par1 > '\u0000' && i != -1 && !this.unicodeFlag)
		{
			return this.charWidth[i];
		}
		if(this.glyphWidth[par1] != 0)
		{
			int j = this.glyphWidth[par1] >>> 4;
			int k = this.glyphWidth[par1] & 15;
			if(k > 7)
			{
				k = 15;
				j = 0;
			}
			return (++k - j) / 2 + 1;
		}
		return 0;
	}
	
	public String trimStringToWidth(String par1Str, int par2)
	{
		return this.trimStringToWidth(par1Str, par2, false);
	}
	
	public String trimStringToWidth(String par1Str, int par2, boolean par3)
	{
		StringBuilder stringbuilder = new StringBuilder();
		int j = 0;
		int k = par3 ? par1Str.length() - 1 : 0;
		int l = par3 ? -1 : 1;
		boolean flag1 = false;
		boolean flag2 = false;
		for(int i1 = k; i1 >= 0 && i1 < par1Str.length() && j < par2; i1 += l)
		{
			char c0 = par1Str.charAt(i1);
			int j1 = this.getCharWidth(c0);
			if(flag1)
			{
				flag1 = false;
				if(c0 != 'l' && c0 != 'L')
				{
					if(c0 == 'r' || c0 == 'R')
					{
						flag2 = false;
					}
				} else
				{
					flag2 = true;
				}
			} else if(j1 < 0)
			{
				flag1 = true;
			} else
			{
				j += j1;
				if(flag2)
				{
					++j;
				}
			}
			if(j > par2)
				break;
			if(par3)
			{
				stringbuilder.insert(0, c0);
				continue;
			}
			stringbuilder.append(c0);
		}
		return stringbuilder.toString();
	}
	
	private String trimStringNewline(String par1Str)
	{
		while(par1Str != null && par1Str.endsWith("\n"))
		{
			par1Str = par1Str.substring(0, par1Str.length() - 1);
		}
		return par1Str;
	}
	
	public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5)
	{
		this.resetStyles();
		this.textColor = par5;
		par1Str = this.trimStringNewline(par1Str);
		this.renderSplitString(par1Str, par2, par3, par4, false);
	}
	
	public List drawSplitString(String par1Str, int par2, int par3, int par4, int par5, Gui gui, ITooltipContext ctx)
	{
		this.resetStyles();
		this.textColor = par5;
		par1Str = this.trimStringNewline(par1Str);
		return renderSplitString(par1Str, par2, par3, par4, false, gui, ctx);
	}
	
	private List renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5, Gui gui, ITooltipContext ctx)
	{
		List tooltip = new ArrayList<>();
		
		GlStateManager.enableTexture2D();
		
		List<String> list = this.listFormattedStringToWidth(par1Str, par4);
		Iterator<String> iterator = list.iterator();
		while(iterator.hasNext())
		{
			String s1 = iterator.next();
			if(s1.contains("@"))
			{
				int i1 = s1.indexOf("@");
				int i2 = s1.indexOf("@", i1 + 1);
				if(i1 >= 0 && i2 > i1)
				{
					int index = Integer.parseInt(s1.substring(i1 + 1, i2));
					s1 = s1.replaceAll(s1.substring(i1, i2 + 1), "").trim();
					String s2 = inserts.size() - 1 >= index ? this.inserts.get(index) : null;
					if(s2 != null)
					{
						if(s2.contains("<LINE>") || s2.contains("<LINE/>"))
						{
							UtilsFX.bindTexture("hammercore", "textures/gui/gui_manual_page.png");
							gui.drawTexturedModalRect(par2 + par4 / 2 - 48, par3 + 2, 24, 184, 96, 4);
						} else if(s2.contains("<IMG>"))
						{
							String cont = s2.replace("<IMG>", "");
							cont = cont.replace("</IMG>", "");
							String[] scont = cont.split(":");
							UtilsFX.bindTexture(scont[0], scont[1]);
							float scale = Float.parseFloat(scont[6]);
							GL11.glPushMatrix();
							GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
							GL11.glTranslatef(par2 - 3 + par4 / 2 - Integer.parseInt(scont[4]) / 2 * scale, par3, 0.0f);
							GL11.glScalef(scale, scale, scale);
							gui.drawTexturedModalRect(0, 0, Integer.parseInt(scont[2]), Integer.parseInt(scont[3]), Integer.parseInt(scont[4]), Integer.parseInt(scont[5]));
							GL11.glPopMatrix();
							par3 = (int) (par3 + (Integer.parseInt(scont[5]) * scale - this.FONT_HEIGHT));
						}
					}
				}
			} else
				renderStringAligned(TextFormatting.RESET + s1, par2, par3, par4, this.textColor, par5);
			par3 += FONT_HEIGHT;
		}
		
		return tooltip;
	}
	
	private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5)
	{
		List<String> list = this.listFormattedStringToWidth(par1Str, par4);
		for(String s1 : list)
		{
			this.renderStringAligned(s1, par2, par3, par4, this.textColor, par5);
			par3 += this.FONT_HEIGHT;
		}
	}
	
	public int splitStringWidth(String par1Str, int par2)
	{
		return this.FONT_HEIGHT * this.listFormattedStringToWidth(par1Str, par2).size();
	}
	
	public void setUnicodeFlag(boolean par1)
	{
		this.unicodeFlag = par1;
	}
	
	public boolean getUnicodeFlag()
	{
		return this.unicodeFlag;
	}
	
	public void setBidiFlag(boolean par1)
	{
		this.bidiFlag = par1;
	}
	
	public List<String> listFormattedStringToWidth(String par1Str, int par2)
	{
		this.inserts.clear();
		int count = 0;
		boolean found = true;
		while(found)
		{
			found = false;
			par1Str = par1Str.replaceAll("<BR>", "\n");
			if((par1Str = par1Str.replaceAll("<BR/>", "\n")).indexOf("<LINE>") >= 0 || par1Str.indexOf("<LINE/>") >= 0)
			{
				this.inserts.add("<LINE>");
				par1Str = par1Str.indexOf("<LINE>") >= 0 ? par1Str.replaceFirst("<LINE>", "\n@" + count + "@\n") : par1Str.replaceFirst("<LINE/>", "\n@" + count + "@\n");
				++count;
				found = true;
			} else if(par1Str.indexOf("<IMG>") >= 0)
			{
				int i1 = par1Str.indexOf("<IMG>");
				int i2 = par1Str.indexOf("</IMG>");
				String s = par1Str.substring(i1, i2) + "</IMG>";
				this.inserts.add(s);
				par1Str = par1Str.replaceFirst(s, "\n@" + count + "@\n");
				++count;
				found = true;
			}
		}
		return Arrays.asList(this.wrapFormattedStringToWidth(par1Str, par2).split("\n"));
	}
	
	String wrapFormattedStringToWidth(String par1Str, int par2)
	{
		int j = this.sizeStringToWidth(par1Str, par2);
		if(par1Str.length() <= j)
			return par1Str;
		String s1 = par1Str.substring(0, j);
		char c0 = par1Str.charAt(j);
		boolean flag = c0 == ' ' || c0 == '\n';
		String s2 = HCFontRenderer.getFormatFromString(s1) + par1Str.substring(j + (flag ? 1 : 0));
		return s1 + "\n" + this.wrapFormattedStringToWidth(s2, par2);
	}
	
	private int sizeStringToWidth(String par1Str, int par2)
	{
		int l;
		int j = par1Str.length();
		int k = 0;
		int i1 = -1;
		boolean flag = false;
		for(l = 0; l < j; ++l)
		{
			char c0 = par1Str.charAt(l);
			switch(c0)
			{
			case '\u00a7':
			{
				char c1;
				if(l >= j - 1)
					break;
				if((c1 = par1Str.charAt(++l)) != 'l' && c1 != 'L')
				{
					if(c1 != 'r' && c1 != 'R' && !HCFontRenderer.isFormatColor(c1))
						break;
					flag = false;
					break;
				}
				flag = true;
				break;
			}
			case ' ':
			{
				i1 = l;
			}
			default:
			{
				k += this.getCharWidth(c0);
				if(!flag)
					break;
				++k;
			}
			}
			if(c0 == '\n')
			{
				i1 = ++l;
				break;
			}
			if(k > par2)
				break;
		}
		return l != j && i1 != -1 && i1 < l ? i1 : l;
	}
	
	private static boolean isFormatColor(char par0)
	{
		return par0 >= '0' && par0 <= '9' || par0 >= 'a' && par0 <= 'f' || par0 >= 'A' && par0 <= 'F';
	}
	
	private static boolean isFormatSpecial(char par0)
	{
		return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
	}
	
	private static String getFormatFromString(String par0Str)
	{
		String s1 = "";
		int i = -1;
		int j = par0Str.length();
		while((i = par0Str.indexOf(167, i + 1)) != -1)
		{
			if(i >= j - 1)
				continue;
			char c0 = par0Str.charAt(i + 1);
			if(HCFontRenderer.isFormatColor(c0))
			{
				s1 = "\u00a7" + c0;
				continue;
			}
			if(!HCFontRenderer.isFormatSpecial(c0))
				continue;
			s1 = s1 + "\u00a7" + c0;
		}
		return s1;
	}
	
	public boolean getBidiFlag()
	{
		return this.bidiFlag;
	}
	
	public static interface ITooltipContext
	{
		int getStartX();
		
		int getStartY();
		
		int getMouseX();
		
		int getMouseY();
	}
}