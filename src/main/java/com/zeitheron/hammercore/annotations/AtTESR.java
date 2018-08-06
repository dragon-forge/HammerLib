package com.zeitheron.hammercore.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.zeitheron.hammercore.client.render.tesr.TESR;

import net.minecraft.tileentity.TileEntity;

/**
 * Makes automatic registering for {@link TESR} for chosen {@link TileEntity}
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface AtTESR
{
	Class<? extends TileEntity> value();
}