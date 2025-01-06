package org.zeith.hammerlib.client.utils;

import java.util.Locale;

public enum FluidTextureType
{
	STILL,
	FLOWING;
	
	public static FluidTextureType fromString(String s)
	{
		return switch(s.toLowerCase(Locale.ROOT))
		{
			case "flow", "flowing" -> FLOWING;
			case "still" -> STILL;
			default -> null;
		};
	}
}