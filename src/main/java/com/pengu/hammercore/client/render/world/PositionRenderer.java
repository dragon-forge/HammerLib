package com.pengu.hammercore.client.render.world;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PositionRenderer
{
	protected double posX, posY, posZ, renderDistance = Double.POSITIVE_INFINITY;
	protected int dimension;
	protected boolean isDead;
	
	public PositionRenderer(double posX, double posY, double posZ, int dimension)
	{
		this.dimension = dimension;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public double calcX()
	{
		return posX - staticPlayerX;
	}
	
	public double calcY()
	{
		return posY - staticPlayerY;
	}
	
	public double calcZ()
	{
		return posZ - staticPlayerZ;
	}
	
	public boolean isDead()
	{
		return isDead;
	}
	
	public boolean canRender(EntityPlayer player)
	{
		return dimension == player.world.provider.getDimension() && player.getDistance(posX, posY, posZ) <= renderDistance;
	}
	
	public void render(EntityPlayer player, double x, double y, double z)
	{
		
	}
}