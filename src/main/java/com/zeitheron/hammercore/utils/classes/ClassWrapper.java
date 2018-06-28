package com.zeitheron.hammercore.utils.classes;

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
	
	public static String getCallerClassName()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for(int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if(!ste.getClassName().equals(ClassWrapper.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0)
				return ste.getClassName();
		}
		return null;
	}
	
	public static Class<?> getCallerClass()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for(int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if(!ste.getClassName().equals(ClassWrapper.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0)
				try
				{
					return Class.forName(ste.getClassName());
				} catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
		}
		return null;
	}
}