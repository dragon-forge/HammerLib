package org.zeith.hammerlib.api.config;

import net.minecraftforge.api.distmarker.Dist;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Config
{
	String module() default "";

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface ConfigEntry
	{
		String comment() default "";

		String entry() default "";
	}

	/**
	 * Marks a config entry as a non-syncable, meaning it won't get written to/from the network.
	 */
	@Target(FIELD)
	@Retention(RUNTIME)
	@interface AvoidSync
	{
	}

	/**
	 * Marks a config entry as loadable only in one environment, which also prevents it from being synced.
	 */
	@Target(FIELD)
	@Retention(RUNTIME)
	@interface LoadOnlyIn
	{
		Dist value();
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface IntEntry
	{
		int value();

		int min() default Integer.MIN_VALUE;

		int max() default Integer.MAX_VALUE;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface LongEntry
	{
		long value();

		long min() default Long.MIN_VALUE;

		long max() default Long.MAX_VALUE;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface FloatEntry
	{
		float value();

		float min() default Float.NaN;

		float max() default Float.NaN;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface DoubleEntry
	{
		double value();

		double min() default Double.NaN;

		double max() default Double.NaN;
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface StringEntry
	{
		String value();

		String[] allowed() default {};
	}

	@Target(FIELD)
	@Retention(RUNTIME)
	@interface BooleanEntry
	{
		boolean value();
	}
}