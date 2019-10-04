package com.zeitheron.hammercore.client.witty;

import net.minecraft.client.resources.I18n;

public interface IWittyComment
{
	String get();
	
	static IWittyComment ofStatic(String text)
	{
		return () -> text;
	}
	
	static IWittyComment translated(String text, Object... parameters)
	{
		return () -> I18n.format(text, parameters);
	}
	
	static IWittyComment delayed(long ms, String... lines)
	{
		final long frs = ms * lines.length;
		return () -> lines[(int) ((System.currentTimeMillis() % frs) / ms)];
	}
}