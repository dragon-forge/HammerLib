package org.zeith.hammerlib.util.configured.struct;

import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.struct.mappers.*;
import org.zeith.hammerlib.util.configured.struct.reflection.IField;
import org.zeith.hammerlib.util.configured.types.*;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import org.zeith.hammerlib.util.java.tuples.Tuples;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ConfigStructure
{
	private static final Map<Class<?>, ITokenMapper<?, ?>> TOKENS_BY_TYPE = new HashMap<>();
	
	static
	{
		registerTokenWriter(new IntegerMapper<>(byte.class, BigInteger::byteValue));
		registerTokenWriter(new IntegerMapper<>(Byte.class, BigInteger::byteValue));
		registerTokenWriter(new IntegerMapper<>(short.class, BigInteger::shortValue));
		registerTokenWriter(new IntegerMapper<>(Short.class, BigInteger::shortValue));
		registerTokenWriter(new IntegerMapper<>(long.class, BigInteger::longValue));
		registerTokenWriter(new IntegerMapper<>(Long.class, BigInteger::longValue));
		registerTokenWriter(new IntegerMapper<>(int.class, BigInteger::intValue));
		registerTokenWriter(new IntegerMapper<>(Integer.class, BigInteger::intValue));
		registerTokenWriter(new IntegerMapper<>(BigInteger.class, UnaryOperator.identity()));
		
		registerTokenWriter(new DecimalMapper<>(float.class, BigDecimal::floatValue));
		registerTokenWriter(new DecimalMapper<>(Float.class, BigDecimal::floatValue));
		registerTokenWriter(new DecimalMapper<>(double.class, BigDecimal::doubleValue));
		registerTokenWriter(new DecimalMapper<>(Double.class, BigDecimal::doubleValue));
		registerTokenWriter(new DecimalMapper<>(BigDecimal.class, UnaryOperator.identity()));
		
		registerTokenWriter(new StringMapper());
	}
	
	public static synchronized void registerTokenWriter(ITokenMapper<?, ?> writer)
	{
		TOKENS_BY_TYPE.put(writer.getType(), writer);
	}
	
	@SuppressWarnings("unchecked")
	public static <A extends ConfigElement<A>, T> ITokenMapper<A, T> getToken(Class<T> type)
	{
		if(type.isArray())
		{
			ITokenMapper<A, ?> sub = getToken(type.getComponentType());
			return sub == null ? null : (ITokenMapper<A, T>) sub.arrayOf();
		}
		
		return (ITokenMapper<A, T>) TOKENS_BY_TYPE.get(type);
	}
	
	public static <T> T createConfig(Supplier<T> newInstance, ConfigObject<?> obj, boolean setDefault)
	{
		return apply(newInstance.get(), obj, setDefault);
	}
	
	public static <T> T apply(T instance, ConfigObject<?> obj, boolean setDefault)
	{
		for(Method method : instance.getClass().getMethods())
			if(method.isAnnotationPresent(SetDefaults.class) && method.getParameterCount() == 0)
				try
				{
					method.setAccessible(true);
					method.invoke(instance);
				} catch(IllegalAccessException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
		
		for(Field field : instance.getClass().getFields())
		{
			if(field.isAnnotationPresent(Unconfigurable.class)) continue;
			Name nameAnn = field.getAnnotation(Name.class);
			String name = nameAnn != null ? nameAnn.value() : field.getName();
			loadField(IField.wrap(field, instance), name, obj, setDefault);
		}
		
		return instance;
	}
	
	@SuppressWarnings({
			"rawtypes",
			"unchecked",
			"RedundantCast"
	})
	private static <A extends ConfigElement<A>, T> void loadField(IField<T> field, String name, ConfigObject<?> obj, boolean setDefault)
	{
		Tuple3.Mutable3<A, T, ITokenMapper<A, T>> tup = obtain(field, name, obj);
		
		if(tup == null)
		{
			T b = field.get();
			if(Modifier.isFinal(field.getModifiers()) && b != null)
			{
				ConfigCategory sub = obj.getElement(ConfiguredLib.CATEGORY, name);
				field.annotation(Comment.class).map(Comment::value).ifPresent(sub::withComment);
				apply(b, sub, setDefault);
			}
			
			return;
		}
		
		field.annotation(Comment.class).map(Comment::value).ifPresent(tup.a()::withComment);
		if(setDefault)
			((ITokenMapper) tup.c()).defaultValue(tup.a(), field, field.get());
		field.set(obtain(field, name, obj).b());
	}
	
	private static <A extends ConfigElement<A>, T> Tuple3.Mutable3<A, T, ITokenMapper<A, T>> obtain(IField<T> field, String name, ConfigObject<?> obj)
	{
		ITokenMapper<A, T> mapper = getToken(field.getType());
		if(mapper == null) return null;
		A a = obj.getElement(mapper.getToken(), name);
		return Tuples.mutable(a, mapper.apply(a), mapper);
	}
}