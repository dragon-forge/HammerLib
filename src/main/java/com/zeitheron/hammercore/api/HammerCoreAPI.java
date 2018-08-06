package com.zeitheron.hammercore.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows any class to be defined as an instance for API between HammerCore and
 * used class. Test for presence in {@link APILoader}
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface HammerCoreAPI
{
	String name();
	
	String version();
}