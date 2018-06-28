package com.zeitheron.hammercore.utils.classes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper
{
	private Object inst;
	private Method meth;
	
	public MethodWrapper(Object inst, Method meth)
	{
		if(meth != null)
			meth.setAccessible(true);
		this.inst = inst;
		this.meth = meth;
	}
	
	public Object call(Object... params)
	{
		if(meth != null)
			try
			{
				return meth.invoke(inst, params);
			} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				return e;
			}
		return null;
	}
}