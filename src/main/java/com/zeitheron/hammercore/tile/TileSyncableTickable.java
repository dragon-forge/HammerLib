package com.zeitheron.hammercore.tile;

import com.zeitheron.hammercore.cfg.tickslip.TickSlipConfig;
import com.zeitheron.hammercore.net.props.NetPropertyAbstract;
import net.minecraft.util.ITickable;

import java.util.Random;

public abstract class TileSyncableTickable
		extends TileSyncable
		implements ITickable
{
	public int changes = 0;
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
		tickPrevValues();
		if(ticksExisted % TickSlipConfig.getTickRate(getBlockType()) == 0) tick();

		if(changes > 0)
		{
			changes = 0;
			sync();
		}
	}

	/**
	 * Use this to update prevX = x, handy with new tick slip feature.
	 */
	public void tickPrevValues()
	{
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