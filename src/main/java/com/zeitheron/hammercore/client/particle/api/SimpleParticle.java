package com.zeitheron.hammercore.client.particle.api;

import com.zeitheron.hammercore.proxy.ParticleProxy_Client;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class SimpleParticle extends Particle implements IRenderedParticle
{
	public SimpleParticle(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
	}
	
	public SimpleParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
	}
	
	public void spawn()
	{
		ParticleProxy_Client.queueParticleSpawn(this);
	}
	
	@Override
	public void setMaxAge(int age)
	{
		particleMaxAge = age;
	}
	
	public void setARGB(int argb)
	{
		particleAlpha = ((argb >> 24) & 255) / 255F;
		particleRed = ((argb >> 16) & 255) / 255F;
		particleGreen = ((argb >> 8) & 255) / 255F;
		particleBlue = ((argb >> 0) & 255) / 255F;
	}
	
	public void setRGB(int rgb)
	{
		particleRed = ((rgb >> 16) & 255) / 255F;
		particleGreen = ((rgb >> 8) & 255) / 255F;
		particleBlue = ((rgb >> 0) & 255) / 255F;
	}
	
	@Override
	public abstract void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ);
	
	private boolean render;
	
	@Override
	public final void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		render = true;
	}
	
	@Override
	public boolean isRendered()
	{
		return render;
	}
	
	@Override
	public void setRendered()
	{
		render = false;
	}
}