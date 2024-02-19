package org.zeith.hammerlib.annotations.ap;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
public @interface RegisterAP
{
	Class<? extends Annotation> value();
	
	boolean clientOnly() default false;
}