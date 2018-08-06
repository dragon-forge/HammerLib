package com.zeitheron.hammercore.utils.color;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Lets you get dye color by passing {@link ItemStack}
 */
public class ColorUtil
{
	private static final String[] defDyeNames = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
	
	/**
	 * Gets if this stack is a "DyeStack"
	 * 
	 * @param stack
	 *            The stack
	 * @return If the stack is a dye
	 */
	public static boolean isDye(ItemStack stack)
	{
		int[] oreIDs = OreDictionary.getOreIDs(stack);
		List<String> ores = new ArrayList<String>();
		for(int id : oreIDs)
			ores.add(OreDictionary.getOreName(id));
		for(String dye : defDyeNames)
			if(ores.contains("dye" + dye))
				return true;
		return false;
	}
	
	/**
	 * Gets this stack's dye color, or null if it is not a dye
	 * 
	 * @param stack
	 *            The stack that we should get color from
	 * @return The color, or null.
	 */
	public static EnumDyeColor getDyeColor(ItemStack stack)
	{
		int[] oreIDs = OreDictionary.getOreIDs(stack);
		List<String> ores = new ArrayList<String>();
		for(int id : oreIDs)
			ores.add(OreDictionary.getOreName(id));
		int meta = 0;
		for(String dye : defDyeNames)
		{
			if(ores.contains("dye" + dye))
				return EnumDyeColor.byMetadata(15 - meta);
			meta++;
		}
		return null;
	}
	
	/**
	 * Gets a Hex integer containing a color for passed stack
	 * 
	 * @param stack
	 *            The stack that we should get color from
	 * @return The color of the dye, or white
	 */
	public static int getDyeColorInt(ItemStack stack)
	{
		int[] oreIDs = OreDictionary.getOreIDs(stack);
		List<String> ores = new ArrayList<String>();
		for(int id : oreIDs)
			ores.add(OreDictionary.getOreName(id));
		int meta = 0;
		for(String dye : defDyeNames)
		{
			if(ores.contains("dye" + dye))
				return ItemDye.DYE_COLORS[meta];
			meta++;
		}
		return 0xFFFFFF;
	}
	
	/**
	 * Formats message, replacing &[CC] with color codes.
	 * 
	 * @param src
	 *            The string to be formatted
	 * @return The formatted string
	 */
	public static String format(String src)
	{
		for(TextFormatting f : TextFormatting.values())
		{
			String s = f.toString();
			src = src.replaceAll("&" + s.substring(1), s);
		}
		return src;
	}
	
	/**
	 * Inverts ARGB color, passed as a [0;1] float array
	 * 
	 * @param rgb
	 *            The source float array
	 * @return The inverted float array
	 */
	public static float[] reverse(float... rgb)
	{
		for(int i = 0; i < rgb.length; ++i)
			rgb[i] = 1F - rgb[i];
		return rgb;
	}
	
	public static float[] checkHandReverse(EnumHand hand, float... rgb)
	{
		return hand == EnumHand.MAIN_HAND ? rgb : reverse(rgb);
	}
}