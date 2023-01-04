package org.zeith.hammerlib.util.configured.struct;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntryString
{
	String value();
}