package com.pengu.hammercore.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.minecraft.tileentity.TileEntity;

/**
 * Makes automatic registering for
 * {@link com.pengu.hammercore.client.render.tesr.TESR}
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface AtTESR
{
	Class<? extends TileEntity> value();
}