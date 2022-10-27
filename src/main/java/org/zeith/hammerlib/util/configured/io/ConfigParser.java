package org.zeith.hammerlib.util.configured.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.BiPredicate;

public class ConfigParser
{
	private final BufferedReader reader;
	private int tabulation;
	
	public ConfigParser(BufferedReader reader)
	{
		this.reader = reader;
	}
	
	public void mark(int limit) throws IOException
	{
		reader.mark(limit);
	}
	
	public void rewindToMark() throws IOException
	{
		reader.reset();
	}
	
	public int nextCharacter() throws IOException
	{
		return reader.read();
	}
	
	public String readWhile(BiPredicate<StringBuilder, Character> test) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int c;
		while((c = nextCharacter()) >= 0 && test.test(sb, (char) c))
			sb.append(c);
		return sb.toString();
	}
}