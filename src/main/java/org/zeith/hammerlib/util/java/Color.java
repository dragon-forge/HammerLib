package org.zeith.hammerlib.util.java;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;

// AWT color shaded into HammerLib to avoid loading Toolkit on headless servers.
public class Color
		implements Serializable
{
	/**
	 * The color white.  In the default sRGB space.
	 */
	public static final Color white = new Color(255, 255, 255);
	
	/**
	 * The color white.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color WHITE = white;
	
	/**
	 * The color light gray.  In the default sRGB space.
	 */
	public static final Color lightGray = new Color(192, 192, 192);
	
	/**
	 * The color light gray.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color LIGHT_GRAY = lightGray;
	
	/**
	 * The color gray.  In the default sRGB space.
	 */
	public static final Color gray = new Color(128, 128, 128);
	
	/**
	 * The color gray.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color GRAY = gray;
	
	/**
	 * The color dark gray.  In the default sRGB space.
	 */
	public static final Color darkGray = new Color(64, 64, 64);
	
	/**
	 * The color dark gray.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color DARK_GRAY = darkGray;
	
	/**
	 * The color black.  In the default sRGB space.
	 */
	public static final Color black = new Color(0, 0, 0);
	
	/**
	 * The color black.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color BLACK = black;
	
	/**
	 * The color red.  In the default sRGB space.
	 */
	public static final Color red = new Color(255, 0, 0);
	
	/**
	 * The color red.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color RED = red;
	
	/**
	 * The color pink.  In the default sRGB space.
	 */
	public static final Color pink = new Color(255, 175, 175);
	
	/**
	 * The color pink.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color PINK = pink;
	
	/**
	 * The color orange.  In the default sRGB space.
	 */
	public static final Color orange = new Color(255, 200, 0);
	
	/**
	 * The color orange.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color ORANGE = orange;
	
	/**
	 * The color yellow.  In the default sRGB space.
	 */
	public static final Color yellow = new Color(255, 255, 0);
	
	/**
	 * The color yellow.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color YELLOW = yellow;
	
	/**
	 * The color green.  In the default sRGB space.
	 */
	public static final Color green = new Color(0, 255, 0);
	
	/**
	 * The color green.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color GREEN = green;
	
	/**
	 * The color magenta.  In the default sRGB space.
	 */
	public static final Color magenta = new Color(255, 0, 255);
	
	/**
	 * The color magenta.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color MAGENTA = magenta;
	
	/**
	 * The color cyan.  In the default sRGB space.
	 */
	public static final Color cyan = new Color(0, 255, 255);
	
	/**
	 * The color cyan.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color CYAN = cyan;
	
	/**
	 * The color blue.  In the default sRGB space.
	 */
	public static final Color blue = new Color(0, 0, 255);
	
	/**
	 * The color blue.  In the default sRGB space.
	 *
	 * @since 1.4
	 */
	public static final Color BLUE = blue;
	
	/**
	 * The color value.
	 *
	 * @serial
	 * @see #getRGB
	 */
	int value;
	
	/**
	 * The color value in the default sRGB {@code ColorSpace} as
	 * {@code float} components (no alpha).
	 * If {@code null} after object construction, this must be an
	 * sRGB color constructed with 8-bit precision, so compute from the
	 * {@code int} color value.
	 *
	 * @serial
	 * @see #getRGBColorComponents
	 * @see #getRGBComponents
	 */
	private float[] frgbvalue = null;
	
	/**
	 * The color value in the native {@code ColorSpace} as
	 * {@code float} components (no alpha).
	 * If {@code null} after object construction, this must be an
	 * sRGB color constructed with 8-bit precision, so compute from the
	 * {@code int} color value.
	 *
	 * @serial
	 * @see #getRGBColorComponents
	 * @see #getRGBComponents
	 */
	private float[] fvalue = null;
	
	/**
	 * The alpha value as a {@code float} component.
	 * If {@code frgbvalue} is {@code null}, this is not valid
	 * data, so compute from the {@code int} color value.
	 *
	 * @serial
	 * @see #getRGBComponents
	 * @see #getComponents
	 */
	private float falpha = 0.0f;
	
	/**
	 * The {@code ColorSpace}.  If {@code null}, then it's
	 * default is sRGB.
	 *
	 * @serial
	 * @see #getColor
	 * @see #getColorSpace
	 * @see #getColorComponents
	 */
	private ColorSpace cs = null;
	
	/**
	 * Use serialVersionUID from JDK 1.1 for interoperability.
	 */
	@Serial
	private static final long serialVersionUID = 118526816881161077L;
	
	/**
	 * Checks the color integer components supplied for validity.
	 * Throws an {@link IllegalArgumentException} if the value is out of
	 * range.
	 *
	 * @param r
	 * 		the Red component
	 * @param g
	 * 		the Green component
	 * @param b
	 * 		the Blue component
	 **/
	private static void testColorValueRange(int r, int g, int b, int a)
	{
		boolean rangeError = false;
		String badComponentString = "";
		
		if(a < 0 || a > 255)
		{
			rangeError = true;
			badComponentString = badComponentString + " Alpha";
		}
		if(r < 0 || r > 255)
		{
			rangeError = true;
			badComponentString = badComponentString + " Red";
		}
		if(g < 0 || g > 255)
		{
			rangeError = true;
			badComponentString = badComponentString + " Green";
		}
		if(b < 0 || b > 255)
		{
			rangeError = true;
			badComponentString = badComponentString + " Blue";
		}
		if(rangeError == true)
		{
			throw new IllegalArgumentException("Color parameter outside of expected range:"
											   + badComponentString);
		}
	}
	
	/**
	 * Checks the color {@code float} components supplied for
	 * validity.
	 * Throws an {@code IllegalArgumentException} if the value is out
	 * of range.
	 *
	 * @param r
	 * 		the Red component
	 * @param g
	 * 		the Green component
	 * @param b
	 * 		the Blue component
	 **/
	private static void testColorValueRange(float r, float g, float b, float a)
	{
		boolean rangeError = false;
		String badComponentString = "";
		if(a < 0.0 || a > 1.0)
		{
			rangeError = true;
			badComponentString = badComponentString + " Alpha";
		}
		if(r < 0.0 || r > 1.0)
		{
			rangeError = true;
			badComponentString = badComponentString + " Red";
		}
		if(g < 0.0 || g > 1.0)
		{
			rangeError = true;
			badComponentString = badComponentString + " Green";
		}
		if(b < 0.0 || b > 1.0)
		{
			rangeError = true;
			badComponentString = badComponentString + " Blue";
		}
		if(rangeError == true)
		{
			throw new IllegalArgumentException("Color parameter outside of expected range:"
											   + badComponentString);
		}
	}
	
	/**
	 * Creates an opaque sRGB color with the specified red, green,
	 * and blue values in the range (0 - 255).
	 * The actual color used in rendering depends
	 * on finding the best match given the color space
	 * available for a given output device.
	 * Alpha is defaulted to 255.
	 *
	 * @param r
	 * 		the red component
	 * @param g
	 * 		the green component
	 * @param b
	 * 		the blue component
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code r}, {@code g}
	 * 		or {@code b} are outside of the range
	 * 		0 to 255, inclusive
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getRGB
	 */
	public Color(int r, int g, int b)
	{
		this(r, g, b, 255);
	}
	
	/**
	 * Creates an sRGB color with the specified red, green, blue, and alpha
	 * values in the range (0 - 255).
	 *
	 * @param r
	 * 		the red component
	 * @param g
	 * 		the green component
	 * @param b
	 * 		the blue component
	 * @param a
	 * 		the alpha component
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code r}, {@code g},
	 *        {@code b} or {@code a} are outside of the range
	 * 		0 to 255, inclusive
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getAlpha
	 * @see #getRGB
	 */
	@ConstructorProperties({ "red", "green", "blue", "alpha" })
	public Color(int r, int g, int b, int a)
	{
		value = ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8) |
				((b & 0xFF) << 0);
		testColorValueRange(r, g, b, a);
	}
	
	/**
	 * Creates an opaque sRGB color with the specified combined RGB value
	 * consisting of the red component in bits 16-23, the green component
	 * in bits 8-15, and the blue component in bits 0-7.  The actual color
	 * used in rendering depends on finding the best match given the
	 * color space available for a particular output device.  Alpha is
	 * defaulted to 255.
	 *
	 * @param rgb
	 * 		the combined RGB components
	 *
	 * @see ColorModel#getRGBdefault
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getRGB
	 */
	public Color(int rgb)
	{
		value = 0xff000000 | rgb;
	}
	
	/**
	 * Creates an sRGB color with the specified combined RGBA value consisting
	 * of the alpha component in bits 24-31, the red component in bits 16-23,
	 * the green component in bits 8-15, and the blue component in bits 0-7.
	 * If the {@code hasalpha} argument is {@code false}, alpha
	 * is defaulted to 255.
	 *
	 * @param rgba
	 * 		the combined RGBA components
	 * @param hasalpha
	 *        {@code true} if the alpha bits are valid;
	 *        {@code false} otherwise
	 *
	 * @see ColorModel#getRGBdefault
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getAlpha
	 * @see #getRGB
	 */
	public Color(int rgba, boolean hasalpha)
	{
		if(hasalpha)
		{
			value = rgba;
		} else
		{
			value = 0xff000000 | rgba;
		}
	}
	
	/**
	 * Creates an opaque sRGB color with the specified red, green, and blue
	 * values in the range (0.0 - 1.0).  Alpha is defaulted to 1.0.  The
	 * actual color used in rendering depends on finding the best
	 * match given the color space available for a particular output
	 * device.
	 *
	 * @param r
	 * 		the red component
	 * @param g
	 * 		the green component
	 * @param b
	 * 		the blue component
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code r}, {@code g}
	 * 		or {@code b} are outside of the range
	 * 		0.0 to 1.0, inclusive
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getRGB
	 */
	public Color(float r, float g, float b)
	{
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5));
		testColorValueRange(r, g, b, 1.0f);
		frgbvalue = new float[3];
		frgbvalue[0] = r;
		frgbvalue[1] = g;
		frgbvalue[2] = b;
		falpha = 1.0f;
		fvalue = frgbvalue;
	}
	
	/**
	 * Creates an sRGB color with the specified red, green, blue, and
	 * alpha values in the range (0.0 - 1.0).  The actual color
	 * used in rendering depends on finding the best match given the
	 * color space available for a particular output device.
	 *
	 * @param r
	 * 		the red component
	 * @param g
	 * 		the green component
	 * @param b
	 * 		the blue component
	 * @param a
	 * 		the alpha component
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code r}, {@code g}
	 *        {@code b} or {@code a} are outside of the range
	 * 		0.0 to 1.0, inclusive
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @see #getAlpha
	 * @see #getRGB
	 */
	public Color(float r, float g, float b, float a)
	{
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
		frgbvalue = new float[3];
		frgbvalue[0] = r;
		frgbvalue[1] = g;
		frgbvalue[2] = b;
		falpha = a;
		fvalue = frgbvalue;
	}
	
	/**
	 * Creates a color in the specified {@code ColorSpace}
	 * with the color components specified in the {@code float}
	 * array and the specified alpha.  The number of components is
	 * determined by the type of the {@code ColorSpace}.  For
	 * example, RGB requires 3 components, but CMYK requires 4
	 * components.
	 *
	 * @param cspace
	 * 		the {@code ColorSpace} to be used to
	 * 		interpret the components
	 * @param components
	 * 		an arbitrary number of color components
	 * 		that is compatible with the {@code ColorSpace}
	 * @param alpha
	 * 		alpha value
	 *
	 * @throws IllegalArgumentException
	 * 		if any of the values in the
	 *        {@code components} array or {@code alpha} is
	 * 		outside of the range 0.0 to 1.0
	 * @see #getComponents
	 * @see #getColorComponents
	 */
	public Color(ColorSpace cspace, float[] components, float alpha)
	{
		boolean rangeError = false;
		String badComponentString = "";
		int n = cspace.getNumComponents();
		fvalue = new float[n];
		for(int i = 0; i < n; i++)
		{
			if(components[i] < 0.0 || components[i] > 1.0)
			{
				rangeError = true;
				badComponentString = badComponentString + "Component " + i
									 + " ";
			} else
			{
				fvalue[i] = components[i];
			}
		}
		if(alpha < 0.0 || alpha > 1.0)
		{
			rangeError = true;
			badComponentString = badComponentString + "Alpha";
		} else
		{
			falpha = alpha;
		}
		if(rangeError)
		{
			throw new IllegalArgumentException(
					"Color parameter outside of expected range: " +
					badComponentString);
		}
		frgbvalue = cspace.toRGB(fvalue);
		cs = cspace;
		value = ((((int) (falpha * 255)) & 0xFF) << 24) |
				((((int) (frgbvalue[0] * 255)) & 0xFF) << 16) |
				((((int) (frgbvalue[1] * 255)) & 0xFF) << 8) |
				((((int) (frgbvalue[2] * 255)) & 0xFF) << 0);
	}
	
	/**
	 * Returns the red component in the range 0-255 in the default sRGB
	 * space.
	 *
	 * @return the red component.
	 *
	 * @see #getRGB
	 */
	public int getRed()
	{
		return (getRGB() >> 16) & 0xFF;
	}
	
	/**
	 * Returns the green component in the range 0-255 in the default sRGB
	 * space.
	 *
	 * @return the green component.
	 *
	 * @see #getRGB
	 */
	public int getGreen()
	{
		return (getRGB() >> 8) & 0xFF;
	}
	
	/**
	 * Returns the blue component in the range 0-255 in the default sRGB
	 * space.
	 *
	 * @return the blue component.
	 *
	 * @see #getRGB
	 */
	public int getBlue()
	{
		return (getRGB() >> 0) & 0xFF;
	}
	
	/**
	 * Returns the alpha component in the range 0-255.
	 *
	 * @return the alpha component.
	 *
	 * @see #getRGB
	 */
	public int getAlpha()
	{
		return (getRGB() >> 24) & 0xff;
	}
	
	/**
	 * Returns the RGB value representing the color in the default sRGB
	 * {@link ColorModel}.
	 * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
	 * blue).
	 *
	 * @return the RGB value of the color in the default sRGB
	 * {@code ColorModel}.
	 *
	 * @see ColorModel#getRGBdefault
	 * @see #getRed
	 * @see #getGreen
	 * @see #getBlue
	 * @since 1.0
	 */
	public int getRGB()
	{
		return value;
	}
	
	private static final double FACTOR = 0.7;
	
	/**
	 * Creates a new {@code Color} that is a brighter version of this
	 * {@code Color}.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this {@code Color} to create a brighter version
	 * of this {@code Color}.
	 * The {@code alpha} value is preserved.
	 * Although {@code brighter} and
	 * {@code darker} are inverse operations, the results of a
	 * series of invocations of these two methods might be inconsistent
	 * because of rounding errors.
	 *
	 * @return a new {@code Color} object that is
	 * a brighter version of this {@code Color}
	 * with the same {@code alpha} value.
	 *
	 * @see Color#darker
	 * @since 1.0
	 */
	public Color brighter()
	{
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int alpha = getAlpha();
		
		/* From 2D group:
		 * 1. black.brighter() should return grey
		 * 2. applying brighter to blue will always return blue, brighter
		 * 3. non pure color (non zero rgb) will eventually return white
		 */
		int i = (int) (1.0 / (1.0 - FACTOR));
		if(r == 0 && g == 0 && b == 0)
		{
			return new Color(i, i, i, alpha);
		}
		if(r > 0 && r < i) r = i;
		if(g > 0 && g < i) g = i;
		if(b > 0 && b < i) b = i;
		
		return new Color(Math.min((int) (r / FACTOR), 255),
				Math.min((int) (g / FACTOR), 255),
				Math.min((int) (b / FACTOR), 255),
				alpha
		);
	}
	
	/**
	 * Creates a new {@code Color} that is a darker version of this
	 * {@code Color}.
	 * <p>
	 * This method applies an arbitrary scale factor to each of the three RGB
	 * components of this {@code Color} to create a darker version of
	 * this {@code Color}.
	 * The {@code alpha} value is preserved.
	 * Although {@code brighter} and
	 * {@code darker} are inverse operations, the results of a series
	 * of invocations of these two methods might be inconsistent because
	 * of rounding errors.
	 *
	 * @return a new {@code Color} object that is
	 * a darker version of this {@code Color}
	 * with the same {@code alpha} value.
	 *
	 * @see Color#brighter
	 * @since 1.0
	 */
	public Color darker()
	{
		return new Color(Math.max((int) (getRed() * FACTOR), 0),
				Math.max((int) (getGreen() * FACTOR), 0),
				Math.max((int) (getBlue() * FACTOR), 0),
				getAlpha()
		);
	}
	
	/**
	 * Computes the hash code for this {@code Color}.
	 *
	 * @return a hash code value for this object.
	 *
	 * @since 1.0
	 */
	public int hashCode()
	{
		return value;
	}
	
	/**
	 * Determines whether another object is equal to this
	 * {@code Color}.
	 * <p>
	 * The result is {@code true} if and only if the argument is not
	 * {@code null} and is a {@code Color} object that has the same
	 * red, green, blue, and alpha values as this object.
	 *
	 * @param obj
	 * 		the object to test for equality with this
	 *        {@code Color}
	 *
	 * @return {@code true} if the objects are the same;
	 * {@code false} otherwise.
	 *
	 * @since 1.0
	 */
	public boolean equals(Object obj)
	{
		return obj instanceof Color && ((Color) obj).getRGB() == this.getRGB();
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
	}
	
	public static Color decode(String nm)
			throws NumberFormatException
	{
		Integer intval = Integer.decode(nm);
		int i = intval.intValue();
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}
	
	public static Color getColor(String nm)
	{
		return getColor(nm, null);
	}
	
	public static Color getColor(String nm, Color v)
	{
		Integer intval = Integer.getInteger(nm);
		if(intval == null)
		{
			return v;
		}
		int i = intval.intValue();
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}
	
	public static Color getColor(String nm, int v)
	{
		Integer intval = Integer.getInteger(nm);
		int i = (intval != null) ? intval.intValue() : v;
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, (i >> 0) & 0xFF);
	}
	
	public static int HSBtoRGB(float hue, float saturation, float brightness)
	{
		int r = 0, g = 0, b = 0;
		if(saturation == 0)
		{
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else
		{
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch((int) h)
			{
				case 0:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
	}
	
	public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals)
	{
		float hue, saturation, brightness;
		if(hsbvals == null)
		{
			hsbvals = new float[3];
		}
		int cmax = (r > g) ? r : g;
		if(b > cmax) cmax = b;
		int cmin = (r < g) ? r : g;
		if(b < cmin) cmin = b;
		
		brightness = ((float) cmax) / 255.0f;
		if(cmax != 0)
			saturation = ((float) (cmax - cmin)) / ((float) cmax);
		else
			saturation = 0;
		if(saturation == 0)
			hue = 0;
		else
		{
			float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
			float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
			float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
			if(r == cmax)
				hue = bluec - greenc;
			else if(g == cmax)
				hue = 2.0f + redc - bluec;
			else
				hue = 4.0f + greenc - redc;
			hue = hue / 6.0f;
			if(hue < 0)
				hue = hue + 1.0f;
		}
		hsbvals[0] = hue;
		hsbvals[1] = saturation;
		hsbvals[2] = brightness;
		return hsbvals;
	}
	
	public static Color getHSBColor(float h, float s, float b)
	{
		return new Color(HSBtoRGB(h, s, b));
	}
	
	public float[] getRGBComponents(float[] compArray)
	{
		float[] f;
		if(compArray == null)
		{
			f = new float[4];
		} else
		{
			f = compArray;
		}
		if(frgbvalue == null)
		{
			f[0] = ((float) getRed()) / 255f;
			f[1] = ((float) getGreen()) / 255f;
			f[2] = ((float) getBlue()) / 255f;
			f[3] = ((float) getAlpha()) / 255f;
		} else
		{
			f[0] = frgbvalue[0];
			f[1] = frgbvalue[1];
			f[2] = frgbvalue[2];
			f[3] = falpha;
		}
		return f;
	}
	
	public float[] getRGBColorComponents(float[] compArray)
	{
		float[] f;
		if(compArray == null)
		{
			f = new float[3];
		} else
		{
			f = compArray;
		}
		if(frgbvalue == null)
		{
			f[0] = ((float) getRed()) / 255f;
			f[1] = ((float) getGreen()) / 255f;
			f[2] = ((float) getBlue()) / 255f;
		} else
		{
			f[0] = frgbvalue[0];
			f[1] = frgbvalue[1];
			f[2] = frgbvalue[2];
		}
		return f;
	}
	
	public float[] getComponents(float[] compArray)
	{
		if(fvalue == null)
			return getRGBComponents(compArray);
		float[] f;
		int n = fvalue.length;
		if(compArray == null)
		{
			f = new float[n + 1];
		} else
		{
			f = compArray;
		}
		for(int i = 0; i < n; i++)
		{
			f[i] = fvalue[i];
		}
		f[n] = falpha;
		return f;
	}
	
	public float[] getColorComponents(float[] compArray)
	{
		if(fvalue == null)
			return getRGBColorComponents(compArray);
		float[] f;
		int n = fvalue.length;
		if(compArray == null)
		{
			f = new float[n];
		} else
		{
			f = compArray;
		}
		for(int i = 0; i < n; i++)
		{
			f[i] = fvalue[i];
		}
		return f;
	}
	
	public float[] getComponents(ColorSpace cspace, float[] compArray)
	{
		if(cs == null)
		{
			cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		}
		float[] f;
		if(fvalue == null)
		{
			f = new float[3];
			f[0] = ((float) getRed()) / 255f;
			f[1] = ((float) getGreen()) / 255f;
			f[2] = ((float) getBlue()) / 255f;
		} else
		{
			f = fvalue;
		}
		float[] tmp = cs.toCIEXYZ(f);
		float[] tmpout = cspace.fromCIEXYZ(tmp);
		if(compArray == null)
		{
			compArray = new float[tmpout.length + 1];
		}
		for(int i = 0; i < tmpout.length; i++)
		{
			compArray[i] = tmpout[i];
		}
		if(fvalue == null)
		{
			compArray[tmpout.length] = ((float) getAlpha()) / 255f;
		} else
		{
			compArray[tmpout.length] = falpha;
		}
		return compArray;
	}
	
	public float[] getColorComponents(ColorSpace cspace, float[] compArray)
	{
		if(cs == null)
		{
			cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		}
		float[] f;
		if(fvalue == null)
		{
			f = new float[3];
			f[0] = ((float) getRed()) / 255f;
			f[1] = ((float) getGreen()) / 255f;
			f[2] = ((float) getBlue()) / 255f;
		} else
		{
			f = fvalue;
		}
		float[] tmp = cs.toCIEXYZ(f);
		float[] tmpout = cspace.fromCIEXYZ(tmp);
		if(compArray == null)
		{
			return tmpout;
		}
		for(int i = 0; i < tmpout.length; i++)
		{
			compArray[i] = tmpout[i];
		}
		return compArray;
	}
	
	public ColorSpace getColorSpace()
	{
		if(cs == null)
		{
			cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		}
		return cs;
	}
	
	public int getTransparency()
	{
		int alpha = getAlpha();
		if(alpha == 0xff)
		{
			return Transparency.OPAQUE;
		} else if(alpha == 0)
		{
			return Transparency.BITMASK;
		} else
		{
			return Transparency.TRANSLUCENT;
		}
	}
}