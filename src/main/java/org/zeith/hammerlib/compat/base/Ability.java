package org.zeith.hammerlib.compat.base;

import org.zeith.hammerlib.util.java.Cast;

import java.util.Objects;
import java.util.Optional;

public class Ability<T>
{
	private final Class<T> type;
	
	public Ability(Class<T> type)
	{
		this.type = type;
	}
	
	public Optional<T> findIn(Object... items)
	{
		return Cast.firstInstanceof(type, items);
	}
	
	public Class<T> type()
	{
		return type;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Ability) obj;
		return Objects.equals(this.type, that.type);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(type);
	}
	
	@Override
	public String toString()
	{
		return "Ability[" +
				"type=" + type + ']';
	}
	
}