package com.zeitheron.hammercore.mod;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface ModuleLoader
{
	String value() default "";
	
	String requiredModid() default "hammercore";
}