package org.zeith.hammerlib.core.js.math;

import com.google.gson.JsonElement;
import org.zeith.hammerlib.core.js.ExpressionParser;

@FunctionalInterface
public interface InterpolatedDouble<T extends IVariableAccess>
{
	double get(T query);
	
	static <T extends IVariableAccess> InterpolatedDouble<T> one()
	{
		return constant(1);
	}
	
	static <T extends IVariableAccess> InterpolatedDouble<T> zero()
	{
		return constant(0);
	}
	
	static <T extends IVariableAccess> InterpolatedDouble<T> constant(double d)
	{
		return query -> d;
	}
	
	static <T extends IVariableAccess> InterpolatedDouble<T> parse(String expression)
	{
		return ExpressionParser.parse(expression);
	}
	
	static <T extends IVariableAccess> InterpolatedDouble<T> parse(Object o)
	{
		if(o instanceof Number)
			return constant(((Number) o).doubleValue());
		if(o instanceof String)
			return parse((String) o);
		if(o instanceof JsonElement)
		{
			JsonElement e = (JsonElement) o;
			return parse(e.getAsString());
		}
		return null;
	}
	
	class NumberWrapped<T extends IVariableAccess>
			extends Number
			implements InterpolatedDouble<T>
	{
		protected final InterpolatedDouble<T> id;
		protected Double value = 0D;
		
		public NumberWrapped(InterpolatedDouble<T> id)
		{
			this.id = id;
		}
		
		public void update(T access)
		{
			this.value = id.get(access);
		}
		
		@Override
		public int intValue()
		{
			return value.intValue();
		}
		
		@Override
		public long longValue()
		{
			return value.longValue();
		}
		
		@Override
		public float floatValue()
		{
			return value.floatValue();
		}
		
		@Override
		public double doubleValue()
		{
			return value;
		}
		
		@Override
		public double get(T query)
		{
			return id.get(query);
		}
	}
}