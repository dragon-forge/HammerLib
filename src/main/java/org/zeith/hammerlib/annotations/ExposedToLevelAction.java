package org.zeith.hammerlib.annotations;

import org.zeith.hammerlib.abstractions.actions.impl.MethodHandleLevelAction;

import java.lang.annotation.*;

/**
 * This annotation flags method as callable for {@link MethodHandleLevelAction}.
 * <p>
 * This is required to prevent potential security issues when deserializing an instance of method handle.
 * <p>
 * While niche, it can be a security nightmare, thus this annotation is in place.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposedToLevelAction
{
}