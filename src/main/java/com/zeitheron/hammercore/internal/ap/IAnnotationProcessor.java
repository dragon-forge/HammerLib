package com.zeitheron.hammercore.internal.ap;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;


public interface IAnnotationProcessor<T extends Annotation>
{
	default void onPreRegistered(IAPContext context, T annotation, Field field, Object value) {}
	
	default void onPostRegistered(IAPContext context, T annotation, Field field, Object value) {}
	
	default void onScanned(IAPContext context, T annotation, Field field, Object value) {}
	
	default void onScanned(IAPContext context, T annotation, Method method) {}
}