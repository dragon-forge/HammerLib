package org.zeith.hammerlib.core.js;

import java.util.*;
import java.util.function.UnaryOperator;

public class ExpressionFixer
{
	private static final List<UnaryOperator<String>> FIXES = new ArrayList<>();
	
	static
	{
		// Fix dangling math operators matching +-*/%&^|? FOLLOWED BY 0+ whitespaces FOLLOWED BY ) by replacing with just )
		registerFix(input -> input.replaceAll("[+\\-*\\/%&^|?]+\\s*\\)", ")"));
	}
	
	public static void registerFix(UnaryOperator<String> fix)
	{
		FIXES.add(fix);
	}
	
	public static String fixExpression(String input)
	{
		for(UnaryOperator<String> fix : FIXES) input = fix.apply(input);
		return input;
	}
}