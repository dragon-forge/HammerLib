package org.zeith.hammerlib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a TileEntityType constant, and provide a value to {@link net.minecraft.client.renderer.tileentity.TileEntityRenderer} or {@link org.zeith.hammerlib.client.render.tile.ITESR}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface TileRenderer
{
	Class<?> value();
}