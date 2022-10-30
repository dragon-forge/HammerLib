package org.zeith.hammerlib.util.java;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

public class NumberUtils
{
	public enum EnumNumberType
	{
		BYTE(1, Byte::parseByte),
		DOUBLE(8, Double::parseDouble),
		FLOAT(4, Float::parseFloat),
		INT(4, Integer::parseInt),
		LONG(8, Long::parseLong),
		SHORT(2, Short::parseShort),
		UNDEFINED(0, v -> null);
		
		public final int byteAmt;
		private final Function<String, ? extends Number> parser;
		
		EnumNumberType(int byteAmt, Function<String, ? extends Number> parser)
		{
			this.byteAmt = byteAmt;
			this.parser = parser;
		}
		
		public Number parse(String value)
		{
			return parser.apply(value);
		}
	}
	
	public static EnumNumberType getType(Number num)
	{
		if(num instanceof Byte)
			return EnumNumberType.BYTE;
		if(num instanceof Double)
			return EnumNumberType.DOUBLE;
		if(num instanceof Float)
			return EnumNumberType.FLOAT;
		if(num instanceof Integer)
			return EnumNumberType.INT;
		if(num instanceof Long)
			return EnumNumberType.LONG;
		if(num instanceof Short)
			return EnumNumberType.SHORT;
		return EnumNumberType.UNDEFINED;
	}
	
	public static float progressToSinef(float value)
	{
		return (float) Math.sin(Math.toRadians(value * 90));
	}
	
	public static double progressToSined(double value)
	{
		return Math.sin(Math.toRadians(value * 90));
	}
	
	public static float progressToSinef(float value, float maxValue)
	{
		return (float) Math.sin(Math.toRadians(value / maxValue * 90));
	}
	
	public static double progressToSined(double value, double maxValue)
	{
		return Math.sin(Math.toRadians(value / maxValue * 90));
	}
	
	public static byte[] asBytes(Number num)
	{
		EnumNumberType type = getType(num);
		if(type != EnumNumberType.UNDEFINED)
		{
			ByteBuffer buf = ByteBuffer.allocate(type.byteAmt + 1);
			buf.put((byte) type.ordinal());
			if(num instanceof Byte)
				buf.put(num.byteValue());
			if(num instanceof Double)
				buf.putDouble(num.doubleValue());
			if(num instanceof Float)
				buf.putFloat(num.floatValue());
			if(num instanceof Integer)
				buf.putInt(num.intValue());
			if(num instanceof Long)
				buf.putLong(num.longValue());
			if(num instanceof Short)
				buf.putShort(num.shortValue());
			return buf.array();
		}
		return new byte[] { (byte) EnumNumberType.UNDEFINED.ordinal() };
	}
	
	public static Number fromBytes(byte[] array)
	{
		ByteBuffer buf = ByteBuffer.wrap(array);
		if(array.length == 0)
			return null;
		EnumNumberType type = EnumNumberType.values()[buf.get() % EnumNumberType.values().length];
		if(type != EnumNumberType.UNDEFINED)
		{
			if(type == EnumNumberType.BYTE)
				return buf.get();
			if(type == EnumNumberType.DOUBLE)
				return buf.getDouble();
			if(type == EnumNumberType.FLOAT)
				return buf.getFloat();
			if(type == EnumNumberType.INT)
				return buf.getInt();
			if(type == EnumNumberType.LONG)
				return buf.getLong();
			if(type == EnumNumberType.SHORT)
				return buf.getShort();
		}
		return null;
	}
	
	public static Optional<Number> tryParse(String value, EnumNumberType type)
	{
		Number parsed = null;
		try
		{
			parsed = type.parse(value);
		} catch(Throwable err)
		{
		}
		return Optional.ofNullable(parsed);
	}
}