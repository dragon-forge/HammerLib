package org.zeith.hammerlib.util.java;

import com.google.common.collect.Lists;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;

import javax.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class ReflectionUtil
{
	
	public static Class<?> getArrayComponent(Class<?> array)
	{
		return array.isArray() ? getArrayComponent(array.getComponentType()) : array;
	}
	
	public static <T> Class<?> findCommonSuperClass(Collection<T> coll)
	{
		if(coll.isEmpty())
		{
			return Void.class;
		} else
		{
			Class<?> oclass = null;
			
			for(T t : coll)
			{
				if(oclass == null)
					oclass = t.getClass();
				else
					oclass = findClosestAncestor(oclass, t.getClass());
			}
			
			return oclass;
		}
	}
	
	public static <T> Class<?> findCommonSuperClass(Collection<T> coll, Function<T, Class<?>> toClass)
	{
		if(coll.isEmpty())
		{
			return Void.class;
		} else
		{
			Class<?> oclass = null;
			for(T t : coll)
				if(oclass == null)
					oclass = toClass.apply(t);
				else
					oclass = findClosestAncestor(oclass, toClass.apply(t));
			return oclass;
		}
	}
	
	public static Class<?> findClosestAncestor(Class<?> a, Class<?> b)
	{
		while(!a.isAssignableFrom(b))
			a = a.getSuperclass();
		return a;
	}
	
	public static boolean doesParameterTypeArgsMatch(Parameter param, Class<?>... baseArgs)
	{
		java.lang.reflect.Type[] args = getTypeArgs(param.getParameterizedType());
		if(args.length != baseArgs.length)
			return false;
		for(int i = 0; i < args.length; ++i)
			if(!(args[i] instanceof Class) || !baseArgs[i].isAssignableFrom((Class) args[i]))
				return false;
		return true;
	}
	
	public static boolean doesParameterTypeArgsMatch(Field field, Class<?>... baseArgs)
	{
		java.lang.reflect.Type[] args = getTypeArgs(field.getGenericType());
		if(args.length != baseArgs.length)
			return false;
		for(int i = 0; i < args.length; ++i)
			if(!(args[i] instanceof Class) || !baseArgs[i].isAssignableFrom((Class) args[i]))
				return false;
		return true;
	}
	
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
				HammerLib.LOG.warn("Attempted to load class from invalid dist: " + name, e);
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
	
	public static Object lookupValue(Object object, Class<?> type)
	{
		Field field = lookupField(object.getClass(), type);
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
	
	public static Field lookupField(Class<?> clazz, Class<?> type)
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
	
	public static List<Field> lookupFields(Class<?> clazz, Class<?> type)
	{
		List<Field> fields = new ArrayList<>();
		for(Field field : clazz.getDeclaredFields())
		{
			if(type.isAssignableFrom(field.getType()))
			{
				field.setAccessible(true);
				fields.add(field);
			}
		}
		return fields;
	}
	
	public static Field lookupField(Class<?> clazz, String name)
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
	
	public static <T> Optional<T> fetchValue(Field field, Object instance, Class<T> targetType)
	{
		try
		{
			field.setAccessible(true);
			return Cast.optionally(field.get(instance), targetType);
		} catch(Throwable err)
		{
		}
		return Optional.empty();
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
	
	public static <T> Optional<T> getField(Object anything, int i, Class<T> cast)
	{
		try
		{
			Field f = anything.getClass().getDeclaredFields()[i];
			f.setAccessible(true);
			return Cast.optionally(f.get(anything), cast);
		} catch(Throwable e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static <T> Optional<T> getStaticFinalField(Class<?> owner, String member)
	{
		try
		{
			Field f = owner.getDeclaredField(member);
			f.setAccessible(true);
			return Optional.ofNullable(Cast.cast(f.get(null)));
		} catch(ReflectiveOperationException e)
		{
			if(!(e instanceof NoSuchFieldException))
				e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Method findDeclaredMethod(Class<?> c, String member, Predicate<Method> o) throws NoSuchMethodException
	{
		for(Method method : c.getDeclaredMethods())
			if(method.getName().equals(member) && o.test(method))
				return method;
		throw new NoSuchMethodException(c.getName() + "/" + member);
	}
}