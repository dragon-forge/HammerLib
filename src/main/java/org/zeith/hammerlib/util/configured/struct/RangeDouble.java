package org.zeith.hammerlib.util.configured.struct;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeDouble
{
	double min() default Double.MIN_VALUE;
	
	double max() default Double.MAX_VALUE;
}