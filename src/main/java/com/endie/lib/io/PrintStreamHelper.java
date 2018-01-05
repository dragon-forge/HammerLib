package com.endie.lib.io;

import java.io.PrintStream;

public class PrintStreamHelper
{
	public static PrintStream merge(PrintStream... streams)
	{
		return new PrintStream(new MultiOutputStream(streams));
	}
}