package org.zeith.hammerlib.util.configured.io;

import java.io.*;
import java.util.function.IntPredicate;

public class StringReader
{
	public static String readUntilWhitespace(BufferedReader reader) throws IOException
	{
		int read = 1;
		
		reader.mark(read);
		
		StringBuilder sb = new StringBuilder();
		int r;
		while((r = reader.read()) >= 0)
		{
			reader.reset();
			reader.mark(++read);
			reader.skip(read - 1);
			
			if(Character.isWhitespace((char) r))
			{
				if(sb.length() > 0)
					return sb.toString();
				continue;
			}
			
			sb.append((char) r);
		}
		
		return sb.toString();
	}
	
	public static String readName(BufferedReader reader, IntPredicate whitespaceFilter) throws IOException
	{
		return readName(reader, false, whitespaceFilter);
	}
	
	public static String readName(BufferedReader reader, boolean forceEscaping, IntPredicate whitespaceFilter) throws IOException
	{
		reader.mark(1);
		boolean escaping = skipWhitespaces(reader) == '"' || forceEscaping;
		if(!escaping) reader.reset();
		
		boolean isInBackslashState = false;
		
		StringBuilder sb = new StringBuilder();
		int r;
		while((r = reader.read()) >= 0)
		{
			if(escaping)
			{
				if(isInBackslashState)
				{
					if(r == 'n') sb.append('\n');
					else if(r == 't') sb.append('\t');
					else sb.append((char) r);
					
					isInBackslashState = false;
					
					continue;
				}
				
				if(r == '\\')
				{
					isInBackslashState = true;
					continue;
				}
				
				if(r == '"')
				{
					reader.mark(1);
					break;
				}
				
				sb.append((char) r);
			} else
			{
				if(whitespaceFilter.test(r))
				{
					if(sb.length() > 0)
						return sb.toString();
					continue;
				}
				
				sb.append((char) r);
			}
			
			reader.mark(1);
		}
		
		return sb.toString();
	}
	
	public static int skipWhitespaces(BufferedReader reader) throws IOException
	{
		reader.mark(4);
		int r;
		while((r = reader.read()) >= 0 && Character.isWhitespace((char) r))
			reader.mark(4);
		return r;
	}
	
	public static void writeName(BufferedWriter writer, String name, IntPredicate whitespace) throws IOException
	{
		writer.write('"');
		for(char c : name.toCharArray())
		{
			if(c == '"' || c == '\\') writer.write('\\');
			else if(c == '\n')
			{
				writer.write("\\n");
				continue;
			} else if(c == '\r')
			{
				// Skip this character
				continue;
			} else if(c == '\t')
			{
				writer.write("\\t");
				continue;
			}
			writer.write(c);
		}
		writer.write('"');
	}
}