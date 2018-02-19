package com.pengu.hammercore.client.witty;

public interface iWittyComment
{
	String get();
	
	public static iWittyComment ofStatic(String text)
	{
		return () -> text;
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