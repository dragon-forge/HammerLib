package com.zeitheron.hammercore.client;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface UserModule
{
	String username();
}