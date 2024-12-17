package org.zeith.hammerlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface Assumes
{
	String[] value();
}