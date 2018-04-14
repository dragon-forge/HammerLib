package com.pengu.hammercore.common.utils.classes;

import java.lang.reflect.Method;

public class ClassWrapper
{
	public final Class<?> clazz;
	
	public ClassWrapper(Class<?> cls)
	{
		this.clazz = cls;
	}
	
	public FieldWrapper findField(String name)
	{
		try
		{
			return new FieldWrapper<>(getInstance(), clazz.getField(name));
		} catch(NoSuchFieldException | SecurityException e)
		{
			return new FieldWrapper<>(null, null);
		}
	}
	
	public MethodWrapper findMethod(String name, Class... classes)
	{
		try
		{
			return new MethodWrapper(getInstance(), clazz.getMethod(name, classes));
		} catch(NoSuchMethodException | SecurityException e)
		{
			return new MethodWrapper(null, null);
		}
	}
	
	public Object getInstance()
	{
		return null;
	}
	
	public static ClassWrapper create(String cls)
	{
		try
		{
			return new ClassWrapper(Class.forName(cls));
		} catch(Throwable err)
		{
		}
		
		return null;
	}
	
	public static ClassWrapper create(Object inst)
	{
		return new InstanceWrapper(inst);
	}
}