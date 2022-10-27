package org.zeith.hammerlib.util.configured.struct;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeInt
{
	int min() default Integer.MIN_VALUE;
	
	int max() default Integer.MAX_VALUE;
}