package com.zeitheron.hammercore.cfg;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation for {@link IConfigReloadListener}.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface HCModConfigurations
{
	String modid();
	
	String module() default "";
	
	boolean isModule() default false;
}