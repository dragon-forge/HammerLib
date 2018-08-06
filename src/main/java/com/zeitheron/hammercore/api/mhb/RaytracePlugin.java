package com.zeitheron.hammercore.api.mhb;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotate any {@link IRayRegistry} plugin to make it public to Hammer Core and
 * make it call {@link IRayRegistry#registerCubes(IRayCubeRegistry)} when server
 * starts.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface RaytracePlugin
{
}