package com.zeitheron.hammercore.api.lighting.impl;

import com.zeitheron.hammercore.api.lighting.ColoredLight;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

/**
 * Applicable for {@link Entity} or {@link TileEntity}
 */
public interface IGlowingEntity
{
	ColoredLight produceColoredLight(float partialTicks);
}