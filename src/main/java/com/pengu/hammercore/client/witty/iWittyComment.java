package com.pengu.hammercore.client.witty;

import net.minecraft.client.resources.I18n;

public interface iWittyComment
{
	String get();
	
	public static iWittyComment ofStatic(String text)
	{
		return () -> text;
	}
	
	public static iWittyComment translated(String text, Object... parameters)
	{
		return () -> I18n.format(text, parameters).replaceAll("@PERCENT@", "%");
	}
	
	public static iWittyComment delayed(long ms, String... lines)
	{
		final long frs = ms * lines.length;
		return () ->
		{
			int c = (int) ((System.currentTimeMillis() % frs) / ms);
			return lines[c];
		};
	}
}