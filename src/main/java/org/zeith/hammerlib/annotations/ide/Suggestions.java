package org.zeith.hammerlib.annotations.ide;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Suggestions
{
	String[] value();
}