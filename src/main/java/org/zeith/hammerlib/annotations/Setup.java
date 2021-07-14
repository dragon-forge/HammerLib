package org.zeith.hammerlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate a method with @Setup, @OnlyIf supported.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Setup
{
}