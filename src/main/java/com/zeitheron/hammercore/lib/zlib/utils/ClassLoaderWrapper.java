package com.zeitheron.hammercore.lib.zlib.utils;

import java.util.HashMap;
import java.util.Map;

public class ClassLoaderWrapper
{
	public final ClassLoader wrapped;
	public final Map<String, Class<?>> acc1 = new HashMap<>();
	public final Map<Class<?>, String> acc2 = new HashMap<>();
	
	public ClassLoaderWrapper(ClassLoader cl)
	{
		this.wrapped = cl;
	}
	
	public Class<?> forName(String str) throws ClassNotFoundException
	{
		if(this.acc1.containsKey(str))
		{
			return this.acc1.get(str);
		}
		return this.wrapped.loadClass(str);
	}
	
	public ClassLoaderWrapper accosiate(String str, Class<?> cl)
	{
		this.acc1.put(str, cl);
		this.acc2.put(cl, str);
		return this;
	}
	
	public String getName(Class<?> cl)
	{
		if(this.acc2.get(cl) != null)
		{
			return this.acc2.get(cl);
		}
		return cl.getName();
	}
	
	public boolean unaccosiate(String str)
	{
		Class<?> cl = this.acc1.remove(str);
		if(cl != null)
		{
			this.acc2.remove(cl);
		}
		if(cl != null)
		{
			return true;
		}
		return false;
	}
}
