package com.zeitheron.hammercore.cfg.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A string config annotation.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ModConfigPropertyString
{
	String category();
	
	String name();
	
	String comment();
	
	String defaultValue();
	
	/** Return either null or empty array to allow anything */
	String[] allowedValues();
}