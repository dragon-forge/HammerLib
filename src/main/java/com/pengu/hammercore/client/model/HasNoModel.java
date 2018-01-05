package com.pengu.hammercore.client.model;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Targets item/block not to be registered into model mesher
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface HasNoModel
{
}