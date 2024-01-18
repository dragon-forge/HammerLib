package org.zeith.hammerlib.annotations;

import org.jetbrains.annotations.*;
import org.zeith.hammerlib.util.java.*;

import java.lang.annotation.*;
import java.util.*;

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