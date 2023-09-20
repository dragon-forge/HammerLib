package org.zeith.hammerlib.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
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
	 * The value must point to a boolean field OR a static method that takes NO arguments and returns a boolean.
	 * Optionally, you can put a dot to let the checker go deeper if necessary.
	 */
	String member() default "";

	boolean invert() default false;
}