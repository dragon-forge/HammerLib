package com.zeitheron.hammercore.client.witty;

import net.minecraft.client.resources.I18n;

public interface IWittyComment
{
	String get();
	
	public static IWittyComment ofStatic(String text)
	{
		return () -> text;
	}
	
	public static IWittyComment translated(String text, Object... parameters)
	{
		return () -> I18n.format(text, parameters);
	}
	
	public static IWittyComment delayed(long ms, String... lines)
	{
		final long frs = ms * lines.length;
		return () ->
		{
			int c = (int) ((System.currentTimeMillis() % frs) / ms);
			return lines[c];
		};
	}
}