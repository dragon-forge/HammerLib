package org.zeith.hammerlib.api.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.util.java.OptionalFloat;

import java.util.*;

public interface IDataTree
{
	@Nullable
	Object get(String key);
	
	@NotNull
	Set<String> keys();
	
	default boolean getBoolean(String index)
	{
		var val = get(index);
		return val instanceof Boolean b ? b : Boolean.parseBoolean(Objects.toString(val));
	}
	
	default boolean getBooleanOrDefault(String index, boolean defaultValue)
	{
		var val = get(index);
		String s;
		return val instanceof Boolean b ? b :
			   ((s = Objects.toString(val)).equalsIgnoreCase("false") || s.equalsIgnoreCase("true")
				? Boolean.parseBoolean(s)
				: defaultValue);
	}
	
	default String getString(String index)
	{
		var val = get(index);
		return val != null ? Objects.toString(val) : null;
	}
	
	default OptionalInt getInt(String index)
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
	
	default OptionalFloat getFloat(String index)
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
	
	default OptionalLong getLong(String index)
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
	
	default OptionalDouble getDouble(String index)
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