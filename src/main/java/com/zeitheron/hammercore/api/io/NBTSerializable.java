package com.zeitheron.hammercore.api.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NBTSerializable
{
	/**
	 * Serialized name (how it is going to be named in the NBT tag) of the annotated field.
	 */
	String value() default "";
}