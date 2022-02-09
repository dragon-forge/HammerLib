package org.zeith.hammerlib.api.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface NBTSerializer
{
	/**
	 * Classes that this serializer supports.
	 */
	Class<?>[] value();
}