package org.zeith.hammerlib.annotations.client;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate a method with @ClientSetup, @OnlyIf supported.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ClientSetup
{
}