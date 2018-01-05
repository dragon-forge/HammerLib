package com.pengu.hammercore.client.particle.api;

import java.util.Set;

import net.minecraft.client.particle.Particle;

public interface iParticleGetter
{
	void addParticles(Set<Particle> particles);
}