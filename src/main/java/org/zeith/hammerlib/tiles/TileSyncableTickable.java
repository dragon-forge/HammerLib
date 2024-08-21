package org.zeith.hammerlib.tiles;

import net.minecraft.util.ITickable;

import java.util.Random;

public abstract class TileSyncableTickable
		extends TileSyncable
		implements ITickable
{
	public int ticksExisted = 0;
	
	/**
	 * If changed in the constructor, may change random's seed.
	 */
	protected boolean positionedRandom = false;
	
	{
		rand = null;
	}
	
	@Override
	public Random getRNG()
	{
		/* Make unique random for each position */
		if(rand == null)
			rand = new Random(positionedRandom && world != null && pos != null ? (world.getSeed() + pos.toLong() + world.provider.getDimension() * 3L - getClass().getName().hashCode()) : new Random().nextLong());
		return rand;
	}
	
	public boolean atTickRate(int rate)
	{
		return ticksExisted % rate == 0;
	}
	
	@Override
	public final void update()
	{
		getRNG();
		ticksExisted++;
		tick();
	}
	
	public void tick()
	{
	}
}