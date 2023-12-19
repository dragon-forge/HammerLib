package org.zeith.hammerlib.annotations;

import net.neoforged.api.distmarker.Dist;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate a method with @Setup, @OnlyIf supported.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Setup
{
	Dist[] side() default {
			Dist.CLIENT,
			Dist.DEDICATED_SERVER
	};
}