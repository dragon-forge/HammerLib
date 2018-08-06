package com.zeitheron.hammercore.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.zeitheron.hammercore.utils.EnumSide;

import net.minecraftforge.common.MinecraftForge;

/**
 * Targets class to be registered to {@link MinecraftForge#EVENT_BUS} at PreInit
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface MCFBus
{
	EnumSide side() default EnumSide.UNIVERSAL;
	
	boolean log() default true;
}