package com.zeitheron.hammercore.annotations;

import java.lang.annotation.Target;

@Target({ })
public @interface FieldReference
{
	Class<?> clazz();
	
	String field();
}