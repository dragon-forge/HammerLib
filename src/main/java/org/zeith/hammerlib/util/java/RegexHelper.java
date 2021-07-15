package org.zeith.hammerlib.util.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper
{
	public static int matchCount(Pattern pat, String input)
	{
		Matcher matcher = pat.matcher(input);
		int count = 0;
		while(matcher.find()) count++;
		return count;
	}
}