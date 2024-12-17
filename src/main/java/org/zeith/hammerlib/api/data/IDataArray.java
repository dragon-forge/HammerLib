package org.zeith.hammerlib.api.data;

import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.OptionalFloat;

import java.util.*;

public interface IDataArray
{
	@Nullable
	Object get(int index);
	
	int length();
	
	default boolean getBoolean(int index)
	{
		var val = get(index);
		return val instanceof Boolean b ? b : Boolean.parseBoolean(Objects.toString(val));
	}
	
	default String getString(int index)
	{
		return Objects.toString(get(index));
	}
	
	default OptionalInt getInt(int index)
	{
		var val = get(index);
		if(val instanceof Number num) return OptionalInt.of(num.intValue());
		if(val instanceof IDataArray || val instanceof IDataTree) return OptionalInt.empty();
		try
		{
			return OptionalInt.of(Integer.parseInt(Objects.toString(val)));
		} catch(Exception e)
		{
			return OptionalInt.empty();
		}
	}
	
	default OptionalFloat getFloat(int index)
	{
		var val = get(index);
		if(val instanceof Number num) return OptionalFloat.of(num.floatValue());
		if(val instanceof IDataArray || val instanceof IDataTree) return OptionalFloat.empty();
		try
		{
			return OptionalFloat.of(Float.parseFloat(Objects.toString(val)));
		} catch(Exception e)
		{
			return OptionalFloat.empty();
		}
	}
	
	default OptionalLong getLong(int index)
	{
		var val = get(index);
		if(val instanceof Number num) return OptionalLong.of(num.longValue());
		if(val instanceof IDataArray || val instanceof IDataTree) return OptionalLong.empty();
		try
		{
			return OptionalLong.of(Long.parseLong(Objects.toString(val)));
		} catch(Exception e)
		{
			return OptionalLong.empty();
		}
	}
	
	default OptionalDouble getDouble(int index)
	{
		var val = get(index);
		if(val instanceof Number num) return OptionalDouble.of(num.doubleValue());
		if(val instanceof IDataArray || val instanceof IDataTree) return OptionalDouble.empty();
		try
		{
			return OptionalDouble.of(Double.parseDouble(Objects.toString(val)));
		} catch(Exception e)
		{
			return OptionalDouble.empty();
		}
	}
}