package org.zeith.hammerlib.util.configured.struct;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeLong
{
	long min() default Long.MIN_VALUE;
	
	long max() default Long.MAX_VALUE;
}