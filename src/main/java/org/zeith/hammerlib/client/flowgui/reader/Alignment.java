package org.zeith.hammerlib.client.flowgui.reader;

import java.util.Locale;
import java.util.NoSuchElementException;

public enum Alignment
{
	START,
	CENTER,
	END;
	
	public float apply(float offset, float parentSize, float thisSize)
	{
		return switch(this)
		{
			case START -> offset;
			case CENTER -> (parentSize - thisSize) / 2;
			case END -> parentSize - thisSize - offset;
		};
	}
	
	public static Alignment readX(String src, Alignment defaultValue)
	{
		if(src == null) return defaultValue;
		src = src.toLowerCase(Locale.ROOT);
		return switch(src)
		{
			case "start", "left" -> START;
			case "center" -> CENTER;
			case "end", "right" -> END;
			default -> throw new NoSuchElementException("Unknown alignment: " + src);
		};
	}
	
	public static Alignment readY(String src, Alignment defaultValue)
	{
		if(src == null) return defaultValue;
		src = src.toLowerCase(Locale.ROOT);
		return switch(src)
		{
			case "start", "top", "up" -> START;
			case "center" -> CENTER;
			case "end", "bottom", "down" -> END;
			default -> throw new NoSuchElementException("Unknown alignment: " + src);
		};
	}
}