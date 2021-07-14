package org.zeith.hammerlib.api.lighting.impl;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.zeith.hammerlib.api.lighting.ColoredLight;

/**
 * Applicable for {@link Entity} or {@link TileEntity} or {@link net.minecraft.client.particle.Particle}
 */
public interface IGlowingEntity
{
	ColoredLight produceColoredLight(float partialTicks);
}