package com.zeitheron.hammercore.cfg.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ModConfigPropertyBool
{
	String category();
	
	String name();
	
	String comment();
	
	boolean defaultValue();
}