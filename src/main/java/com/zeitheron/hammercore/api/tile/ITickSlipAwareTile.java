package com.zeitheron.hammercore.api.tile;

public interface ITickSlipAwareTile
{
	/**
	 * This method is going to get called each tick on a tile entity, even if it's tick rate was reduced.
	 * Call to set previous values to reserve correct interpolation
	 *
	 * @param tickRate The amount of ticks that have to pass before the next tile update method is fired.
	 */
	void handleInterpolations(int tickRate);
}