package com.zeitheron.hammercore.tile;

import java.util.Random;

import com.zeitheron.hammercore.net.props.NetPropertyAbstract;

import net.minecraft.util.ITickable;

public abstract class TileSyncableTickable extends TileSyncable implements ITickable
{
	public int changes = 0;
	public int ticksExisted = 0;
	
	/** If changed in the constructor, may change random's seed. */
	protected boolean positionedRandom = false;
	
	{
		rand = null;
	}
	
	@Override
	public Random getRNG()
	{
		/** Make unique random for each position */
		if(rand == null)
			rand = new Random(positionedRandom && world != null && pos != null ? (world.getSeed() + pos.toLong() + world.provider.getDimension() * 3L - getClass().getName().hashCode()) : world.rand.nextLong());
		return rand;
	}
	
	@Override
	public final void update()
	{
		/* Updates location each tick, if needed. */
		getLocation();
		
		/** Make unique random for each position */
		if(rand == null)
			rand = new Random(positionedRandom ? (world.getSeed() + pos.toLong() + world.provider.getDimension() * 3L) : world.rand.nextLong());
		
		ticksExisted++;
		tick();
		
		if(changes > 0)
		{
			changes = 0;
			sync();
		}
	}
	
	public void tick()
	{
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		changes++;
	}
	
	@Override
	public void sendChangesToNearby()
	{
		changes++;
	}
}