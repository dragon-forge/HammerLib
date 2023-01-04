package org.zeith.hammerlib.util.configured.struct.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public interface IField<T>
{
	int getModifiers();
	
	Class<T> getType();
	
	T get();
	
	void set(T value);
	
	String getName();
	
	<A extends Annotation> Optional<A> annotation(Class<A> type);
	
	static <A> IField<A> wrap(Field field, Object instance)
	{
		return new IField<A>()
		{
			@Override
			public int getModifiers()
			{
				return field.getModifiers();
			}
			
			@Override
			public Class getType()
			{
				return field.getType();
			}
			
			@Override
			public A get()
			{
				try
				{
					field.setAccessible(true);
					return (A) field.get(instance);
				} catch(IllegalAccessException e)
				{
					return null;
				}
			}
			
			@Override
			public void set(A value)
			{
				try
				{
					field.setAccessible(true);
					field.set(instance, value);
				} catch(IllegalAccessException e)
				{
				}
			}
			
			@Override
			public String getName()
			{
				return field.getName();
			}
			
			@Override
			public <A1 extends Annotation> Optional<A1> annotation(Class<A1> type)
			{
				return Optional.ofNullable(field.getAnnotation(type));
			}
		};
	}
}