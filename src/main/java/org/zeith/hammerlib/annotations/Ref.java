package org.zeith.hammerlib.annotations;

import com.google.common.base.Suppliers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.lang.annotation.*;
import java.util.*;
import java.util.function.Supplier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ })
public @interface Ref
{
	Class<?> value();
	
	String field() default "";
	
	class Resolver
	{
		public static boolean isFieldSet(Ref ref)
		{
			return isTypeSet(ref) && !ref.field().isBlank();
		}
		
		public static boolean isTypeSet(Ref ref)
		{
			return !void.class.equals(ref.value());
		}
		
		public static Ref decode(Map<String, Object> ref)
		{
			Supplier<Class<?>> value = Suppliers.memoize(() -> ReflectionUtil.fetchClass((Type) ref.get("value")));
			Supplier<String> member = Suppliers.memoize(() -> (String) ref.getOrDefault("field", ""));
			
			return new Ref()
			{
				@Override
				public Class<?> value()
				{
					return value.get();
				}
				
				@Override
				public String field()
				{
					return member.get();
				}
				
				@Override
				public Class<? extends Annotation> annotationType()
				{
					return Ref.class;
				}
			};
		}
		
		@Nullable
		public static Object resolveField(Ref ref)
		{
			return ReflectionUtil.getStaticFinalField(ref.value(), ref.field()).orElse(null);
		}
		
		@Nullable
		public static <T> T resolveField(Class<T> type, Ref ref)
		{
			return Cast.cast(resolveField(ref), type);
		}
		
		@NotNull
		public static List<Object> resolveFields(Ref[] ref)
		{
			if(ref == null || ref.length == 0) return List.of();
			return Arrays.stream(ref)
					.filter(Ref.Resolver::isFieldSet)
					.map(Ref.Resolver::resolveField)
					.filter(Objects::nonNull)
					.toList();
		}
		
		@NotNull
		public static <T> List<T> resolveFields(Class<T> type, Ref[] refs)
		{
			if(refs == null || refs.length == 0) return List.of();
			return Arrays.stream(refs)
					.filter(Ref.Resolver::isFieldSet)
					.map(ref -> resolveField(type, ref))
					.filter(Objects::nonNull)
					.toList();
		}
	}
}