package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.util.java.Cast;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

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
			case CENTER -> (parentSize - thisSize) / 2 + offset;
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
	
	public static Supplier<Alignment> readX(FlowQuery query, String src, Alignment defaultValue)
	{
		if(src == null) return Cast.constant(defaultValue);
		src = src.toLowerCase(Locale.ROOT);
		return switch(src)
		{
			case "start", "left" -> Cast.constant(START);
			case "center" -> Cast.constant(CENTER);
			case "end", "right" -> Cast.constant(END);
			default -> throw new NoSuchElementException("Unknown alignment: " + src);
		};
	}
	
	public static Supplier<Alignment> readY(FlowQuery query, String src, Alignment defaultValue)
	{
		if(src == null) return Cast.constant(defaultValue);
		src = src.toLowerCase(Locale.ROOT);
		return switch(src)
		{
			case "start", "top", "up" -> Cast.constant(START);
			case "center" -> Cast.constant(CENTER);
			case "end", "bottom", "down" -> Cast.constant(END);
			default -> throw new NoSuchElementException("Unknown alignment: " + src);
		};
	}
}