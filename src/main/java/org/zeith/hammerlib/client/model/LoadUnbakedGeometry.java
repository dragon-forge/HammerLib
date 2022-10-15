package org.zeith.hammerlib.client.model;

import java.lang.annotation.*;

/**
 * Annotating your {@link net.minecraftforge.client.model.geometry.IUnbakedGeometry} class with this interface makes it dynamically added into the game.
 * Make sure that your class contains the default (no-parameter) constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadUnbakedGeometry
{
	String path();
}