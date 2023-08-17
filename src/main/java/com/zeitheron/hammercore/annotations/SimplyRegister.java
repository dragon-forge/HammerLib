package com.zeitheron.hammercore.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimplyRegister
{
	String prefix() default "";
	
	FieldReference creativeTab() default @FieldReference(clazz = Object.class, field = "");
}