package com.pengu.hammercore.api.dynlight;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface iMovable
{
	int getX();
	
	int getY();
	
	int getZ();
	
	World getWorld();
	
	boolean isAlive();
	
	public static class BlockMovable implements iMovable
	{
		public BlockPos pos;
		public World world;
		public boolean isAlive = true;
		
		public BlockMovable(BlockPos pos, World world)
		{
			this.pos = pos;
		}
		
		public void setPos(BlockPos pos)
		{
			this.pos = pos;
		}
		
		public void setWorld(World world)
		{
			this.world = world;
		}
		
		@Override
		public int getX()
		{
			return pos.getX();
		}
		
		@Override
		public int getY()
		{
			return pos.getY();
		}
		
		@Override
		public int getZ()
		{
			return pos.getZ();
		}
		
		@Override
		public World getWorld()
		{
			return world;
		}
		
		@Override
		public boolean isAlive()
		{
			return isAlive;
		}
	}
	
	public static class EntityMovable implements iMovable
	{
		private Entity entity;
		
		public EntityMovable(Entity ent)
		{
			entity = ent;
		}
		
		@Override
		public World getWorld()
		{
			return entity.world;
		}
		
		@Override
		public int getX()
		{
			return (int) Math.floor(entity.posX);
		}
		
		@Override
		public int getY()
		{
			return (int) Math.floor(entity.posY);
		}
		
		@Override
		public int getZ()
		{
			return (int) Math.floor(entity.posZ);
		}
		
		public void setEntity(Entity entity)
		{
			this.entity = entity;
		}
		
		public Entity getEntity()
		{
			return entity;
		}
		
		@Override
		public boolean isAlive()
		{
			return entity != null && !entity.isDead;
		}
	}
}