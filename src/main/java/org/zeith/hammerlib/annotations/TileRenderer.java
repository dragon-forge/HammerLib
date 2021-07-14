package org.zeith.hammerlib.annotations;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a TESR with this to let it get registered at runtime.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface TileRenderer
{
	Class<? extends TileEntityRenderer<?>> value();
}