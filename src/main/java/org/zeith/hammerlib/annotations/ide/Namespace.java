package org.zeith.hammerlib.annotations.ide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Namespace
{
	String value();
}