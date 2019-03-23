package com.zeitheron.hammercore.cfg.file1132;

public class ReaderHelper
{
	String line;
	int pos;
	
	public int getPos()
	{
		return pos;
	}
	
	public ReaderHelper(String line)
	{
		this.line = line;
	}
	
	public int push()
	{
		return pos;
	}
	
	public void pop(int marker)
	{
		pos = marker;
	}
	
	public char eat()
	{
		return eat(false);
	}
	
	public String currentLine()
	{
		int i = line.substring(0, pos).lastIndexOf("\n");
		return line.substring(i + 1, pos);
	}
	
	public char eat(boolean ignoreIndents)
	{
		if(ignoreIndents)
			while(eat(' ', false) || eat('\t', false) || eat('\n', false) || eat('\r', false))
				continue;
			
		if(pos >= line.length())
			return (char) -1;
		++pos;
		return line.charAt(pos - 1);
	}
	
	public boolean eat(String s, boolean ignoreIndents)
	{
		int origin = push();
		for(char c : s.toCharArray())
			if(!eat(c, ignoreIndents))
			{
				pop(origin);
				return false;
			}
		return true;
	}
	
	public boolean eat(char c, boolean ignoreIndents)
	{
		if(pos >= line.length())
			return false;
		
		if(line.charAt(pos) == c)
		{
			++pos;
			return true;
		}
		
		if(ignoreIndents)
		{
			while(eat(' ', false) || eat('\t', false) || eat('\n', false) || eat('\r', false))
				continue;
			return eat(c, false);
		}
		
		return false;
	}
	
	/**
	 * @return null if c is not found it rest text
	 */
	public String until(char c, boolean eat)
	{
		int pos = push();
		StringBuilder sb = new StringBuilder();
		while(pos < line.length())
		{
			char r = eat();
			if(r != (char) -1)
			{
				if(r == c)
					break;
				else
					sb.append(r);
			} else
			{
				if(!eat)
					pop(pos);
				return null;
			}
		}
		if(!eat)
			pop(pos);
		return sb.toString();
	}
	
	public String getRest()
	{
		return line.substring(pos);
	}
}