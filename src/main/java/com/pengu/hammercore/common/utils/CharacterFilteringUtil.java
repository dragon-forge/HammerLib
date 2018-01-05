package com.pengu.hammercore.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Lets you filter excess characters, leaving only symbols, numbers, and english
 * words
 */
public class CharacterFilteringUtil
{
	private static final Set<Character> allowedchars = new HashSet<Character>();
	
	public static final char[] NUM_FILTER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static final char[] SYMBOL_FILTER = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '=', '/', '+', '\'', '\\', '\"', ';', '.', ',', ' ', ':', '<', '>', '[', ']', '{', '}', '_' };
	public static final char[] EN_FILTER = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };
	
	static
	{
		allowedchars.addAll(chars());
	}
	
	private static List<Character> chars()
	{
		List<Character> c = new ArrayList<Character>();
		try
		{
			Field[] fs = CharacterFilteringUtil.class.getFields();
			for(Field f : fs)
			{
				if(f.getType().isAssignableFrom(char[].class))
				{
					char[] charArr = (char[]) f.get(null);
					for(char cs : charArr)
						c.add(cs);
				}
			}
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		return c;
	}
	
	public static boolean charRetained(char in)
	{
		return allowedchars.contains(Character.toLowerCase(in)) || allowedchars.contains(Character.toUpperCase(in));
	}
	
	public static String filter(String input)
	{
		String otp = "";
		for(int i = 0; i < input.length(); ++i)
			if(charRetained(input.charAt(i)))
				otp += input.charAt(i);
		return otp;
	}
}