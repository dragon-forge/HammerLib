package com.pengu.hammercore.cfg.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ModConfigPropertyFloat
{
	String category();
	
	String name();
	
	String comment();
	
	float defaultValue();
	
	float min();
	
	float max();
}