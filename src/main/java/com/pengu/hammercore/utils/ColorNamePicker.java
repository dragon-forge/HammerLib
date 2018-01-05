package com.pengu.hammercore.utils;

import java.awt.Color;
import java.util.ArrayList;

public class ColorNamePicker
{
	public static final ArrayList<Col> colorList = initColorList();
	
	private static ArrayList<Col> initColorList()
	{
		ArrayList<Col> colorList = new ArrayList<Col>();
		colorList.add(new Col("AliceBlue", 0xF0, 0xF8, 0xFF));
		colorList.add(new Col("AntiqueWhite", 0xFA, 0xEB, 0xD7));
		colorList.add(new Col("Aqua", 0x00, 0xFF, 0xFF));
		colorList.add(new Col("Aquamarine", 0x7F, 0xFF, 0xD4));
		colorList.add(new Col("Azure", 0xF0, 0xFF, 0xFF));
		colorList.add(new Col("Beige", 0xF5, 0xF5, 0xDC));
		colorList.add(new Col("Bisque", 0xFF, 0xE4, 0xC4));
		colorList.add(new Col("Black", 0x00, 0x00, 0x00));
		colorList.add(new Col("BlanchedAlmond", 0xFF, 0xEB, 0xCD));
		colorList.add(new Col("Blue", 0x00, 0x00, 0xFF));
		colorList.add(new Col("BlueViolet", 0x8A, 0x2B, 0xE2));
		colorList.add(new Col("Brown", 0xA5, 0x2A, 0x2A));
		colorList.add(new Col("BurlyWood", 0xDE, 0xB8, 0x87));
		colorList.add(new Col("CadetBlue", 0x5F, 0x9E, 0xA0));
		colorList.add(new Col("Chartreuse", 0x7F, 0xFF, 0x00));
		colorList.add(new Col("Chocolate", 0xD2, 0x69, 0x1E));
		colorList.add(new Col("Coral", 0xFF, 0x7F, 0x50));
		colorList.add(new Col("CornflowerBlue", 0x64, 0x95, 0xED));
		colorList.add(new Col("Cornsilk", 0xFF, 0xF8, 0xDC));
		colorList.add(new Col("Crimson", 0xDC, 0x14, 0x3C));
		colorList.add(new Col("Cyan", 0x00, 0xFF, 0xFF));
		colorList.add(new Col("DarkBlue", 0x00, 0x00, 0x8B));
		colorList.add(new Col("DarkCyan", 0x00, 0x8B, 0x8B));
		colorList.add(new Col("DarkGoldenRod", 0xB8, 0x86, 0x0B));
		colorList.add(new Col("DarkGray", 0xA9, 0xA9, 0xA9));
		colorList.add(new Col("DarkGreen", 0x00, 0x64, 0x00));
		colorList.add(new Col("DarkKhaki", 0xBD, 0xB7, 0x6B));
		colorList.add(new Col("DarkMagenta", 0x8B, 0x00, 0x8B));
		colorList.add(new Col("DarkOliveGreen", 0x55, 0x6B, 0x2F));
		colorList.add(new Col("DarkOrange", 0xFF, 0x8C, 0x00));
		colorList.add(new Col("DarkOrchid", 0x99, 0x32, 0xCC));
		colorList.add(new Col("DarkRed", 0x8B, 0x00, 0x00));
		colorList.add(new Col("DarkSalmon", 0xE9, 0x96, 0x7A));
		colorList.add(new Col("DarkSeaGreen", 0x8F, 0xBC, 0x8F));
		colorList.add(new Col("DarkSlateBlue", 0x48, 0x3D, 0x8B));
		colorList.add(new Col("DarkSlateGray", 0x2F, 0x4F, 0x4F));
		colorList.add(new Col("DarkTurquoise", 0x00, 0xCE, 0xD1));
		colorList.add(new Col("DarkViolet", 0x94, 0x00, 0xD3));
		colorList.add(new Col("DeepPink", 0xFF, 0x14, 0x93));
		colorList.add(new Col("DeepSkyBlue", 0x00, 0xBF, 0xFF));
		colorList.add(new Col("DimGray", 0x69, 0x69, 0x69));
		colorList.add(new Col("DodgerBlue", 0x1E, 0x90, 0xFF));
		colorList.add(new Col("FireBrick", 0xB2, 0x22, 0x22));
		colorList.add(new Col("FloralWhite", 0xFF, 0xFA, 0xF0));
		colorList.add(new Col("ForestGreen", 0x22, 0x8B, 0x22));
		colorList.add(new Col("Fuchsia", 0xFF, 0x00, 0xFF));
		colorList.add(new Col("Gainsboro", 0xDC, 0xDC, 0xDC));
		colorList.add(new Col("GhostWhite", 0xF8, 0xF8, 0xFF));
		colorList.add(new Col("Gold", 0xFF, 0xD7, 0x00));
		colorList.add(new Col("GoldenRod", 0xDA, 0xA5, 0x20));
		colorList.add(new Col("Gray", 0x80, 0x80, 0x80));
		colorList.add(new Col("Green", 0x00, 0x80, 0x00));
		colorList.add(new Col("GreenYellow", 0xAD, 0xFF, 0x2F));
		colorList.add(new Col("HoneyDew", 0xF0, 0xFF, 0xF0));
		colorList.add(new Col("HotPink", 0xFF, 0x69, 0xB4));
		colorList.add(new Col("IndianRed", 0xCD, 0x5C, 0x5C));
		colorList.add(new Col("Indigo", 0x4B, 0x00, 0x82));
		colorList.add(new Col("Ivory", 0xFF, 0xFF, 0xF0));
		colorList.add(new Col("Khaki", 0xF0, 0xE6, 0x8C));
		colorList.add(new Col("Lavender", 0xE6, 0xE6, 0xFA));
		colorList.add(new Col("LavenderBlush", 0xFF, 0xF0, 0xF5));
		colorList.add(new Col("LawnGreen", 0x7C, 0xFC, 0x00));
		colorList.add(new Col("LemonChiffon", 0xFF, 0xFA, 0xCD));
		colorList.add(new Col("LightBlue", 0xAD, 0xD8, 0xE6));
		colorList.add(new Col("LightCoral", 0xF0, 0x80, 0x80));
		colorList.add(new Col("LightCyan", 0xE0, 0xFF, 0xFF));
		colorList.add(new Col("LightGoldenRodYellow", 0xFA, 0xFA, 0xD2));
		colorList.add(new Col("LightGray", 0xD3, 0xD3, 0xD3));
		colorList.add(new Col("LightGreen", 0x90, 0xEE, 0x90));
		colorList.add(new Col("LightPink", 0xFF, 0xB6, 0xC1));
		colorList.add(new Col("LightSalmon", 0xFF, 0xA0, 0x7A));
		colorList.add(new Col("LightSeaGreen", 0x20, 0xB2, 0xAA));
		colorList.add(new Col("LightSkyBlue", 0x87, 0xCE, 0xFA));
		colorList.add(new Col("LightSlateGray", 0x77, 0x88, 0x99));
		colorList.add(new Col("LightSteelBlue", 0xB0, 0xC4, 0xDE));
		colorList.add(new Col("LightYellow", 0xFF, 0xFF, 0xE0));
		colorList.add(new Col("Lime", 0x00, 0xFF, 0x00));
		colorList.add(new Col("LimeGreen", 0x32, 0xCD, 0x32));
		colorList.add(new Col("Linen", 0xFA, 0xF0, 0xE6));
		colorList.add(new Col("Magenta", 0xFF, 0x00, 0xFF));
		colorList.add(new Col("Maroon", 0x80, 0x00, 0x00));
		colorList.add(new Col("MediumAquaMarine", 0x66, 0xCD, 0xAA));
		colorList.add(new Col("MediumBlue", 0x00, 0x00, 0xCD));
		colorList.add(new Col("MediumOrchid", 0xBA, 0x55, 0xD3));
		colorList.add(new Col("MediumPurple", 0x93, 0x70, 0xDB));
		colorList.add(new Col("MediumSeaGreen", 0x3C, 0xB3, 0x71));
		colorList.add(new Col("MediumSlateBlue", 0x7B, 0x68, 0xEE));
		colorList.add(new Col("MediumSpringGreen", 0x00, 0xFA, 0x9A));
		colorList.add(new Col("MediumTurquoise", 0x48, 0xD1, 0xCC));
		colorList.add(new Col("MediumVioletRed", 0xC7, 0x15, 0x85));
		colorList.add(new Col("MidnightBlue", 0x19, 0x19, 0x70));
		colorList.add(new Col("MintCream", 0xF5, 0xFF, 0xFA));
		colorList.add(new Col("MistyRose", 0xFF, 0xE4, 0xE1));
		colorList.add(new Col("Moccasin", 0xFF, 0xE4, 0xB5));
		colorList.add(new Col("NavajoWhite", 0xFF, 0xDE, 0xAD));
		colorList.add(new Col("Navy", 0x00, 0x00, 0x80));
		colorList.add(new Col("OldLace", 0xFD, 0xF5, 0xE6));
		colorList.add(new Col("Olive", 0x80, 0x80, 0x00));
		colorList.add(new Col("OliveDrab", 0x6B, 0x8E, 0x23));
		colorList.add(new Col("Orange", 0xFF, 0xA5, 0x00));
		colorList.add(new Col("OrangeRed", 0xFF, 0x45, 0x00));
		colorList.add(new Col("Orchid", 0xDA, 0x70, 0xD6));
		colorList.add(new Col("PaleGoldenRod", 0xEE, 0xE8, 0xAA));
		colorList.add(new Col("PaleGreen", 0x98, 0xFB, 0x98));
		colorList.add(new Col("PaleTurquoise", 0xAF, 0xEE, 0xEE));
		colorList.add(new Col("PaleVioletRed", 0xDB, 0x70, 0x93));
		colorList.add(new Col("PapayaWhip", 0xFF, 0xEF, 0xD5));
		colorList.add(new Col("PeachPuff", 0xFF, 0xDA, 0xB9));
		colorList.add(new Col("Peru", 0xCD, 0x85, 0x3F));
		colorList.add(new Col("Pink", 0xFF, 0xC0, 0xCB));
		colorList.add(new Col("Plum", 0xDD, 0xA0, 0xDD));
		colorList.add(new Col("PowderBlue", 0xB0, 0xE0, 0xE6));
		colorList.add(new Col("Purple", 0x80, 0x00, 0x80));
		colorList.add(new Col("Red", 0xFF, 0x00, 0x00));
		colorList.add(new Col("RosyBrown", 0xBC, 0x8F, 0x8F));
		colorList.add(new Col("RoyalBlue", 0x41, 0x69, 0xE1));
		colorList.add(new Col("SaddleBrown", 0x8B, 0x45, 0x13));
		colorList.add(new Col("Salmon", 0xFA, 0x80, 0x72));
		colorList.add(new Col("SandyBrown", 0xF4, 0xA4, 0x60));
		colorList.add(new Col("SeaGreen", 0x2E, 0x8B, 0x57));
		colorList.add(new Col("SeaShell", 0xFF, 0xF5, 0xEE));
		colorList.add(new Col("Sienna", 0xA0, 0x52, 0x2D));
		colorList.add(new Col("Silver", 0xC0, 0xC0, 0xC0));
		colorList.add(new Col("SkyBlue", 0x87, 0xCE, 0xEB));
		colorList.add(new Col("SlateBlue", 0x6A, 0x5A, 0xCD));
		colorList.add(new Col("SlateGray", 0x70, 0x80, 0x90));
		colorList.add(new Col("Snow", 0xFF, 0xFA, 0xFA));
		colorList.add(new Col("SpringGreen", 0x00, 0xFF, 0x7F));
		colorList.add(new Col("SteelBlue", 0x46, 0x82, 0xB4));
		colorList.add(new Col("Tan", 0xD2, 0xB4, 0x8C));
		colorList.add(new Col("Teal", 0x00, 0x80, 0x80));
		colorList.add(new Col("Thistle", 0xD8, 0xBF, 0xD8));
		colorList.add(new Col("Tomato", 0xFF, 0x63, 0x47));
		colorList.add(new Col("Turquoise", 0x40, 0xE0, 0xD0));
		colorList.add(new Col("Violet", 0xEE, 0x82, 0xEE));
		colorList.add(new Col("Wheat", 0xF5, 0xDE, 0xB3));
		colorList.add(new Col("White", 0xFF, 0xFF, 0xFF));
		colorList.add(new Col("WhiteSmoke", 0xF5, 0xF5, 0xF5));
		colorList.add(new Col("Yellow", 0xFF, 0xFF, 0x00));
		colorList.add(new Col("YellowGreen", 0x9A, 0xCD, 0x32));
		for(Col c : colorList)
		{
			String fi = c.name;
			char[] name = fi.toCharArray();
			int off = 0;
			boolean first = true;
			for(int i = 0; i < name.length; ++i)
				if(Character.isUpperCase(name[i]))
				{
					if(first)
					{
						first = false;
						continue;
					}
					fi = fi.substring(0, i + off) + " " + fi.substring(i + off);
					++off;
				}
			c.name = fi;
		}
		return colorList;
	}
	
	public static String getColorNameFromRgb(int r, int g, int b)
	{
		Col closestMatch = null;
		int minMSE = Integer.MAX_VALUE;
		for(Col c : colorList)
		{
			int mse = c.computeMSE(r, g, b);
			if(mse < minMSE)
			{
				minMSE = mse;
				closestMatch = c;
			}
		}
		
		if(closestMatch != null)
			return closestMatch.getName();
		else
			return "No matched color name.";
	}
	
	public static String getColorNameFromHex(int hexColor)
	{
		int r = (int) (ColorHelper.getRed(hexColor) * 0xFF);
		int g = (int) (ColorHelper.getGreen(hexColor) * 0xFF);
		int b = (int) (ColorHelper.getBlue(hexColor) * 0xFF);
		return getColorNameFromRgb(r, g, b);
	}
	
	public static int colorToHex(Color c)
	{
		return Integer.decode("0x" + Integer.toHexString(c.getRGB()).substring(2));
	}
	
	public static String getColorNameFromColor(Color color)
	{
		return getColorNameFromRgb(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public static class Col
	{
		public int r, g, b;
		public String name;
		
		public Col(String name, int r, int g, int b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
			this.name = name;
		}
		
		public int computeMSE(int pixR, int pixG, int pixB)
		{
			return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b) * (pixB - b)) / 3);
		}
		
		public int getR()
		{
			return r;
		}
		
		public int getG()
		{
			return g;
		}
		
		public int getB()
		{
			return b;
		}
		
		public String getName()
		{
			return name;
		}
	}
}