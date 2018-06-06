package com.zeitheron.hammercore.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ParticleGrouped extends Particle
{
	protected ParticleGrouped(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
	}
	
	public ParticleGrouped(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}
	
	public void doRenderParticle(BufferBuilder buffer, double x, double y, double z, float partialTicks)
	{
		
	}
	
	public void doRenderParticle(BufferBuilder buffer, double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		doRenderParticle(buffer, x, y, z, partialTicks);
	}
	
	public abstract ResourceLocation getTexture();
}