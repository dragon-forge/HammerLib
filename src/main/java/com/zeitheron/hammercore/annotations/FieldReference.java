package com.zeitheron.hammercore.annotations;

public @interface FieldReference
{
	Class<?> clazz();
	
	String field();
}