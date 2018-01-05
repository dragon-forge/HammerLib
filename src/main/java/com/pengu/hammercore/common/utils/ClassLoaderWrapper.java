package com.pengu.hammercore.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ClassLoaderWrapper
{
	public final ClassLoader wrapped;
	public final Map<String, Class<?>> acc1 = new HashMap<>();
	public final Map<Class<?>, String> acc2 = new HashMap<>();
	
	public ClassLoaderWrapper(ClassLoader cl)
	{
		wrapped = cl;
	}
	
	public Class<?> forName(String str) throws ClassNotFoundException
	{
		if(acc1.containsKey(str))
			return acc1.get(str);
		return wrapped.loadClass(str);
	}
	
	public ClassLoaderWrapper accosiate(String str, Class<?> cl)
	{
		acc1.put(str, cl);
		acc2.put(cl, str);
		return this;
	}
	
	public String getName(Class<?> cl)
	{
		if(acc2.get(cl) != null)
			return acc2.get(cl);
		return cl.getName();
	}
	
	public boolean unaccosiate(String str)
	{
		Class<?> cl = acc1.remove(str);
		if(cl != null)
			acc2.remove(cl);
		return cl != null;
	}
}