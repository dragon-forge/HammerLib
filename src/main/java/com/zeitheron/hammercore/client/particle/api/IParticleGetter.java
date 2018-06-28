package com.zeitheron.hammercore.client.particle.api;

import java.util.Set;

import net.minecraft.client.particle.Particle;

public interface IParticleGetter
{
	void addParticles(Set<Particle> particles);
}