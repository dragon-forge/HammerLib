package org.zeith.hammerlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({
		FIELD,
		METHOD
})
public @interface OnlyIf
{
	/**
	 * The value must point to a class where the {@link #member()} is located at.
	 */
	Class<?> owner();

	/**
	 * The value must point to a boolean field OR a static method that takes NO arguments and returns a boolean
	 */
	String member();

	boolean invert() default false;
}