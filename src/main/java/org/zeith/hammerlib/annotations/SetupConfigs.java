package org.zeith.hammerlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Put this annotation to a static method that has 1 argument:
 * <code>{@link org.zeith.hammerlib.util.cfg.ConfigFile}</code>.
 * This will invoke during mod setup to initialize configs.
 * Feel free to save the {@link org.zeith.hammerlib.util.cfg.ConfigFile} to a variable to reload anytime you wish.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface SetupConfigs
{
	String module() default "";
}