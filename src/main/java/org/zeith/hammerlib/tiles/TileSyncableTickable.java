package org.zeith.hammerlib.tiles;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import org.zeith.hammerlib.util.mcf.WorldHelper;

import java.util.Random;

public class TileSyncableTickable
		extends TileSyncable
		implements ITickableTileEntity
{
	/**
	 * If changed in the constructor, may change random's seed.
	 */
	protected boolean positionedRandom = false;
	public int ticksExisted = 0;

	public TileSyncableTickable(TileEntityType<?> type)
	{
		super(type);
		rand = null;
	}

	@Override
	public final void tick()
	{
		update();
		ticksExisted++;
	}

	@Override
	public Random getRNG()
	{
		/** Make unique random for each position */
		if(rand == null)
			rand = new Random(positionedRandom && level != null && worldPosition != null ? ((level instanceof ServerWorld ? ((ServerWorld) level).getSeed() : 0L) + worldPosition.asLong() + WorldHelper.getWorldId(level).hashCode() * 41L - getClass().getName().hashCode()) : System.nanoTime());
		return rand;
	}

	public boolean atTickRate(int rate)
	{
		return ticksExisted % rate == 0;
	}

	public void update()
	{
	}
}