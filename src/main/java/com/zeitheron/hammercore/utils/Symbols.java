package com.zeitheron.hammercore.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.zeitheron.hammercore.HammerCore;

public class Symbols
{
	private static transient final char[] chars;
	
	public static Character getSymbolForNum(Integer curr)
	{
		return new Character(chars[curr % chars.length]);
	}
	
	public static String convert(String chars)
	{
		String m = "";
		for(int i = 0; i < chars.length(); ++i)
			if(chars.charAt(i) == ' ')
				m += " ";
			else
				m += "" + getSymbolForNum(Math.abs(i * chars.length() + (chars.hashCode() * chars.hashCode())));
		return m;
	}
	
	static
	{
		HammerCore.LOG.info("[SYMBOLS] Loading symbols...");
		Stopwatch t = Stopwatch.createStarted();
		
		List<Character> charS = new ArrayList<Character>();
		
		for(int i = '\u3400'; i < '\u4FBF'; ++i)
			charS.add((char) i);
		
		chars = new char[charS.size()];
		for(int ind = 0; ind < chars.length; ++ind)
			chars[ind] = charS.get(ind);
		t.stop();
		
		HammerCore.LOG.info("[SYMBOLS] Loaded " + charS.size() + " symbols in " + t.elapsed(TimeUnit.MILLISECONDS) + " ms");
	}
}