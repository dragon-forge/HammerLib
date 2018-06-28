package com.zeitheron.hammercore.lib.zlib.io;

import java.io.PrintStream;

public class PrintStreamHelper
{
	public static PrintStream merge(PrintStream... streams)
	{
		return new PrintStream(new MultiOutputStream(streams));
	}
}