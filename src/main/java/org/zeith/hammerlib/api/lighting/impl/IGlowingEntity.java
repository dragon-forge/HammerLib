package org.zeith.hammerlib.api.lighting.impl;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.zeith.hammerlib.api.lighting.ColoredLight;

/**
 * Applicable for {@link Entity} or {@link BlockEntity} or {@link Particle}
 */
public interface IGlowingEntity
{
	ColoredLight produceColoredLight(float partialTicks);
}