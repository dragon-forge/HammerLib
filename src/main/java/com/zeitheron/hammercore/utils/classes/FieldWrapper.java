package com.zeitheron.hammercore.utils.classes;

import java.lang.reflect.Field;

public class FieldWrapper<T>
{
	private Object inst;
	private Field field;
	
	public FieldWrapper(Object inst, Field field)
	{
		if(field != null)
			field.setAccessible(true);
		
		this.inst = inst;
		this.field = field;
	}
	
	public T get()
	{
		if(field != null)
			try
			{
				return (T) field.get(inst);
			} catch(IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		return null;
	}
	
	public void set(T t)
	{
		if(field != null)
			try
			{
				field.set(inst, t);
			} catch(IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
	}
}