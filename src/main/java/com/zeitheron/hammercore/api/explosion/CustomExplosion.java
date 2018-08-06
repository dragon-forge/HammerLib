package com.zeitheron.hammercore.api.explosion;

import java.util.List;
import java.util.Random;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.api.IProcess;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A fast and large explosion implementation
 */
public class CustomExplosion implements IProcess
{
	private World worldObj;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private float power;
	private Random random = new Random();
	private final DamageSource entityDamageSource;
	private double expansion = 0;
	public boolean breaksUnbreakableBlocks = false;
	
	public CustomExplosion(World world, int x, int y, int z, float power, DamageSource entityDamageSource)
	{
		this.entityDamageSource = entityDamageSource;
		this.worldObj = world;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.power = power;
		isDead = world.isRemote;
	}
	
	public CustomExplosion setBreaksUnbreakableBlocks(boolean breaksUnbreakableBlocks)
	{
		this.breaksUnbreakableBlocks = breaksUnbreakableBlocks;
		return this;
	}
	
	/**
	 * Creates a big explosion of custom strength and damages entities
	 * if(entityDamageSource != null) If chunks where explosion happens are
	 * unloaded it will manually load them.
	 * 
	 * @return was this explosion created -- is happens on server.
	 */
	public static boolean doExplosionAt(World world, BlockPos pos, float power, DamageSource entityDamageSource)
	{
		if(world != null && world.isBlockLoaded(pos) && pos != null && !world.isRemote)
		{
			HammerCore.updatables.add(new CustomExplosion(world, pos.getX(), pos.getY(), pos.getZ(), power, entityDamageSource));
			return true;
		}
		return false;
	}
	
	/**
	 * Creates a big explosion of custom strength and damages entities
	 * if(entityDamageSource != null) If chunks where explosion happens are
	 * unloaded it will manually load them.
	 * 
	 * @return was this explosion created -- is happens on server.
	 */
	public static boolean doExplosionAt(World world, BlockPos pos, float power, DamageSource entityDamageSource, boolean breaksBedrock)
	{
		if(world != null && world.isBlockLoaded(pos) && pos != null && !world.isRemote)
		{
			HammerCore.updatables.add(new CustomExplosion(world, pos.getX(), pos.getY(), pos.getZ(), power, entityDamageSource).setBreaksUnbreakableBlocks(breaksBedrock));
			return true;
		}
		return false;
	}
	
	public static double getDistanceAtoB(double x1, double z1, double x2, double z2)
	{
		double dx = x1 - x2;
		double dz = z1 - z2;
		return Math.sqrt((dx * dx + dz * dz));
	}
	
	@Override
	public void update()
	{
		int OD = (int) expansion;
		int ID = OD - 1;
		int size = (int) expansion;
		
		for(int x = xCoord - size; x < xCoord + size; x++)
		{
			for(int z = zCoord - size; z < zCoord + size; z++)
			{
				double dist = getDistanceAtoB(x, z, xCoord, zCoord);
				if(dist < OD && dist >= ID)
				{
					float tracePower = power - (float) (expansion / 10D);
					tracePower *= 1F + ((random.nextFloat() - 0.5F) * 0.2);
					HammerCore.updatables.add(new CustomExplosionTrace(worldObj, x, yCoord, z, tracePower, random));
				}
			}
		}
		
		isDead = expansion >= power * 10;
		expansion += 1;
	}
	
	private boolean isDead = false;
	
	@Override
	public boolean isAlive()
	{
		return !isDead;
	}
	
	public class CustomExplosionTrace implements IProcess
	{
		private World worldObj;
		private int xCoord;
		private int yCoord;
		private int zCoord;
		private float power;
		private Random random;
		
		public CustomExplosionTrace(World world, int x, int y, int z, float power, Random random)
		{
			this.worldObj = world;
			this.xCoord = x;
			this.yCoord = y;
			this.zCoord = z;
			this.power = power;
			this.random = random;
		}
		
		@Override
		public void update()
		{
			float energy = power * 10;
			
			if(!worldObj.isBlockLoaded(new BlockPos(xCoord, 16, zCoord)))
				worldObj.getChunk(new BlockPos(xCoord, 16, zCoord)); // load
				                                                     // chunk
				
			for(int y = yCoord; y >= 0 && energy > 0; y--)
			{
				List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(xCoord, y, zCoord, xCoord + 1, y + 1, zCoord + 1));
				
				if(entityDamageSource != null)
					for(Entity entity : entities)
						entity.attackEntityFrom(entityDamageSource, power * 100);
					
				if(energy >= 0)
					worldObj.setBlockToAir(new BlockPos(xCoord, y, zCoord));
				energy -= 0.5F + (0.1F * (yCoord - y));
			}
			
			energy = power * 20;
			yCoord++;
			for(int y = yCoord; y < 255 && energy > 0; y++)
			{
				List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(xCoord, y, zCoord, xCoord + 1, y + 1, zCoord + 1));
				
				if(entityDamageSource != null)
					for(Entity entity : entities)
						entity.attackEntityFrom(entityDamageSource, power * 100);
					
				if(energy >= 0)
				{
					BlockPos pos = new BlockPos(xCoord, y, zCoord);
					if(breaksUnbreakableBlocks || worldObj.getBlockState(pos).getBlockHardness(worldObj, pos) != -1)
						worldObj.setBlockToAir(pos);
				}
				
				energy -= 0.5F + (0.1F * (y - yCoord));
			}
			
			isDead = true;
		}
		
		private boolean isDead = false;
		
		@Override
		public boolean isAlive()
		{
			return !isDead;
		}
	}
}