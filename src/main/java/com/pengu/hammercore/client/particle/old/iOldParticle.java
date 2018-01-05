package com.pengu.hammercore.client.particle.old;

public interface iOldParticle
{
	void setVel(double x, double y, double z);
	
	void spawnAt(double x, double y, double z);
	
	/**
	 * Should be equal to call "spawnAt(posX, posY, posZ)", but "posX", "posY",
	 * "posZ" are protected.
	 */
	void spawn();
	
	void setData(ParticleParam data);
}