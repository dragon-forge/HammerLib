package com.zeitheron.hammercore.internal.ap;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Additional processors for fields inside classes with @{@link com.zeitheron.hammercore.annotations.SimplyRegister} annotation.
 */
public class AnnotationProcessorRegistry
{
	public static final Comparator<IAnnotationProcessor<?>> PRIORITY_COMPARATOR = Comparator.<IAnnotationProcessor<?>>comparingInt(IAnnotationProcessor::priority).reversed();
	private static final Map<Class<? extends Annotation>, List<IAnnotationProcessor<?>>> ANNOTATION_PROCESSORS = new HashMap<>();
	private static final Set<Class<? extends Annotation>> UNSORTED = new HashSet<>();
	
	public static <T extends Annotation> void register(Class<T> annotation, IAnnotationProcessor<T> ap)
	{
		ANNOTATION_PROCESSORS.computeIfAbsent(annotation, v -> new ArrayList<>()).add(ap);
		UNSORTED.add(annotation);
	}
	
	public static List<IAnnotationProcessor<?>> getAnnotationProcessors(Class<? extends Annotation> annotation)
	{
		List<IAnnotationProcessor<?>> lst = ANNOTATION_PROCESSORS.getOrDefault(annotation, Collections.emptyList());
		if(UNSORTED.remove(annotation)) lst.sort(PRIORITY_COMPARATOR);
		return Collections.unmodifiableList(lst);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void fireFieldScan(IAPContext ctx, Field f, Object value)
	{
		if(ANNOTATION_PROCESSORS.isEmpty()) return;
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			for(IAnnotationProcessor ap : getAnnotationProcessors(annotation.annotationType()))
				ap.onScanned(ctx, annotation, f, value);
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void fireMethodScan(IAPContext ctx, Method m)
	{
		if(ANNOTATION_PROCESSORS.isEmpty()) return;
		for(Annotation annotation : m.getDeclaredAnnotations())
		{
			for(IAnnotationProcessor ap : getAnnotationProcessors(annotation.annotationType()))
				ap.onScanned(ctx, annotation, m);
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void fireRegister(IAPContext ctx, Field f, Object value, boolean postReg)
	{
		if(ANNOTATION_PROCESSORS.isEmpty()) return;
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			for(IAnnotationProcessor ap : getAnnotationProcessors(annotation.annotationType()))
			{
				if(postReg)
					ap.onPostRegistered(ctx, annotation, f, value);
				else
					ap.onPreRegistered(ctx, annotation, f, value);
			}
		}
	}
}