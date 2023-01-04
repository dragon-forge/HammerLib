package org.zeith.hammerlib.util.configured.struct;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeFloat
{
	float min() default Float.MIN_VALUE;
	
	float max() default Float.MAX_VALUE;
}