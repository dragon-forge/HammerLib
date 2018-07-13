package com.zeitheron.hammercore.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.zeitheron.hammercore.utils.EnumSide;

/**
 * Targets class to be registered to MinecraftForge EVENT_BUS on PreInit
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface MCFBus
{
	EnumSide side() default EnumSide.UNIVERSAL;
	
	boolean log() default true;
}