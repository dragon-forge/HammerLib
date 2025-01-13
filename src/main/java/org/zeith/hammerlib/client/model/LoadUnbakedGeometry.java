package org.zeith.hammerlib.client.model;

import net.neoforged.neoforge.client.model.ExtendedUnbakedModel;

import java.lang.annotation.*;

/**
 * Annotating your {@link ExtendedUnbakedModel} class with this interface makes it dynamically added into the game.
 * Make sure that your class contains the default (no-parameter) constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadUnbakedGeometry
{
	String path();
}