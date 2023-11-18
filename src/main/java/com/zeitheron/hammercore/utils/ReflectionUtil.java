package com.zeitheron.hammercore.utils;

import com.google.common.collect.Lists;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.utils.base.Cast;
import org.objectweb.asm.Type;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.List;

public class ReflectionUtil
{
	private static Field modifiersField;
	private static Object reflectionFactory;
	private static Method newFieldAccessor;
	private static Method fieldAccessorSet;
	
	/**
	 * Examples: For List< String> returns [Type(String)]
	 */
	public static java.lang.reflect.Type[] getTypeArgs(java.lang.reflect.Type type)
	{
		if(type instanceof ParameterizedType)
		{
			ParameterizedType pt = (ParameterizedType) type;
			return pt.getActualTypeArguments();
		}
		return new java.lang.reflect.Type[0];
	}
	
	public static Class<?> fetchClassAny(Type type)
	{
		return fetchClass(type.getSort() < Type.ARRAY ? type.getClassName() : type.getInternalName().replace('/', '.'));
	}
	
	public static <T> Class<T> fetchClass(Type type)
	{
		return fetchClass(type.getSort() < Type.ARRAY ? type.getClassName() : type.getInternalName().replace('/', '.'));
	}
	
	public static <T> Class<T> fetchClass(String name)
	{
		try
		{
			return Cast.cast(Class.forName(name));
		} catch(ClassNotFoundException ignored)
		{
		} catch(RuntimeException e)
		{
			if(e.getMessage().contains("invalid dist"))
			{
				HammerCore.LOG.warn("Attempted to load class from invalid dist: " + name, e);
			}
		}
		return null;
	}
	
	public static Iterable<Field> getFieldsUpTo(@Nonnull Class<?> startClass,
												@Nullable Class<?> exclusiveParent)
	{
		
		List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
		Class<?> parentClass = startClass.getSuperclass();
		
		if(parentClass != null &&
				(exclusiveParent == null || !(parentClass.equals(exclusiveParent))))
		{
			List<Field> parentClassFields =
					(List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
			currentClassFields.addAll(parentClassFields);
		}
		
		return currentClassFields;
	}
	
	public static boolean setStaticFinalField(Class<?> cls, String var, Object val)
	{
		try
		{
			return setStaticFinalField(cls.getDeclaredField(var), val);
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		return false;
	}
	
	public static boolean setStaticFinalField(Field f, Object val)
	{
		try
		{
			if(Modifier.isStatic(f.getModifiers()))
				return setFinalField(f, null, val);
			return false;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		return false;
	}
	
	public static boolean setFinalField(Field f, @Nullable Object instance, Object thing)
			throws ReflectiveOperationException
	{
		if(f == null) return false;
		if(Modifier.isFinal(f.getModifiers()))
		{
			makeWritable(f);
			Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, f, false);
			fieldAccessorSet.invoke(fieldAccessor, instance, thing);
			return true;
		} else
		{
			f.set(instance, thing);
			return true;
		}
	}
	
	private static Field makeWritable(Field f)
			throws ReflectiveOperationException
	{
		f.setAccessible(true);
		if(modifiersField == null)
		{
			Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory");
			reflectionFactory = getReflectionFactory.invoke(null);
			newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, boolean.class);
			fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
		}
		modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
		return f;
	}
	
	public static Object getValue(Object object, Class<?> type)
	{
		Field field = getField(object.getClass(), type);
		if(field == null)
			return null;
		try
		{
			return field.get(object);
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Object getValue(Class<?> object, String fieldName)
	{
		Field field = getField(object, fieldName);
		if(field == null || !Modifier.isStatic(field.getModifiers()))
			return null;
		try
		{
			return field.get(null);
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static Field getField(Class<?> clazz, Class<?> type)
	{
		Field ret = null;
		for(Field field : clazz.getDeclaredFields())
		{
			if(type.isAssignableFrom(field.getType()))
			{
				if(ret != null)
					return null;
				field.setAccessible(true);
				ret = field;
			}
		}
		return ret;
	}
	
	public static Field getFieldByValue(Class<?> clazz, Object instance, Object value)
	{
		for(Field field : clazz.getDeclaredFields())
			if(!Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(value.getClass()))
			{
				field.setAccessible(true);
				try
				{
					if(field.get(instance) == value)
						return field;
				} catch(IllegalAccessException e)
				{
					continue;
				}
			}
		return null;
	}
	
	public static Field getField(Class<?> clazz, String name)
	{
		Field ret = null;
		for(Field field : clazz.getDeclaredFields())
		{
			if(name.equals(field.getName()))
			{
				if(ret != null)
					return null;
				field.setAccessible(true);
				ret = field;
			}
		}
		return ret;
	}
	
	public static Class<?> getCaller()
	{
		try
		{
			return Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
		} catch(ClassNotFoundException e)
		{
			return null;
		}
	}
}