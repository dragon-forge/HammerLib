package org.zeith.hammerlib.util.java;

import com.google.common.collect.Lists;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ReflectionUtil
{
	private static Field modifiersField;
	private static Object reflectionFactory;
	private static Method newFieldAccessor;
	private static Method fieldAccessorSet;

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
		} catch(ClassNotFoundException e)
		{
			return null;
		}
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

	@Deprecated
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

	@Deprecated
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

	@Deprecated
	public static boolean setFinalField(Field f, @Nullable Object instance, Object thing) throws ReflectiveOperationException
	{
		if(Modifier.isFinal(f.getModifiers()))
		{
			makeWritable(f);
			Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, f, false);
			fieldAccessorSet.invoke(fieldAccessor, instance, thing);
			return true;
		} else
			f.set(instance, thing);
		return false;
	}

	@Deprecated
	private static Field makeWritable(Field f) throws ReflectiveOperationException
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
}