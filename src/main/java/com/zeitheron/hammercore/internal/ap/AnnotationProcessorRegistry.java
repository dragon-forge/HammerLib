package com.zeitheron.hammercore.internal.ap;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Additional processors for fields inside classes with @{@link com.zeitheron.hammercore.annotations.SimplyRegister} annotation.
 */
public class AnnotationProcessorRegistry
{
	private static final Map<Class<? extends Annotation>, List<IAnnotationProcessor<?>>> AP = new HashMap<>();
	
	public static <T extends Annotation> void register(Class<T> annotation, IAnnotationProcessor<T> ap)
	{
		AP.computeIfAbsent(annotation, v -> new ArrayList<>()).add(ap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scan(IAPContext ctx, Field f, Object value)
	{
		if(AP.isEmpty()) return;
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
				ap.onScanned(ctx, annotation, f, value);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scanReg(IAPContext ctx, Field f, Object value, boolean postReg)
	{
		if(AP.isEmpty()) return;
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
			{
				if(postReg)
					ap.onPostRegistered(ctx, annotation, f, value);
				else
					ap.onPreRegistered(ctx, annotation, f, value);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scan(IAPContext ctx, Method m)
	{
		if(AP.isEmpty()) return;
		for(Annotation annotation : m.getDeclaredAnnotations())
		{
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
				ap.onScanned(ctx, annotation, m);
		}
	}
}