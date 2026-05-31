package com.zeitheron.hammercore.utils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * @deprecated Use {@link ReflectionUtil} instead. Methods redirected for legacy support.
 */
@Deprecated
public class FinalFieldHelper
{
	public static boolean setStaticFinalField(Class<?> cls, String name, Object val)
	{
		return ReflectionUtil.setStaticFinalField(cls, name, val);
	}
	
	public static boolean setStaticFinalField(Field f, Object val)
	{
		return ReflectionUtil.setStaticFinalField(f, val);
	}
	
	public static boolean setFinalField(Field f, @Nullable Object instance, Object thing) throws ReflectiveOperationException
	{
		return ReflectionUtil.setFinalField(f, instance, thing);
	}
}