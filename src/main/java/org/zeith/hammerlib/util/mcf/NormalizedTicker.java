package org.zeith.hammerlib.util.mcf;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Range;

/**
 * The NormalizedTicker class is responsible for normalizing the tick rate of a certain function.
 * It ensures that the function is only called once per game tick and keeps track of the number of suppressed ticks.
 * Please use it whenever you {@link net.minecraft.world.level.block.entity.BlockEntity} does heavy computations,
 * so that mods that do increase {@link net.minecraft.world.level.block.entity.BlockEntity} tick rate won't cause too much pain for sever owners.
 * We care about them.
 *
 * @author Zeith
 */
public class NormalizedTicker
{
	/**
	 * The current game time
	 */
	protected long gameTime;
	
	/**
	 * The number of suppressed ticks.
	 */
	protected int suppressed;
	
	/**
	 * The tick function to be normalized.
	 */
	protected final NormalizedTickFunction tick;
	
	/**
	 * Constructs a new instance of the NormalizedTicker class.
	 *
	 * @param tick
	 * 		The tick function to be normalized.
	 */
	protected NormalizedTicker(NormalizedTickFunction tick)
	{
		this.tick = tick;
	}
	
	/**
	 * Ticks the normalized function. Call this from your non-normalized function.
	 *
	 * @param level
	 * 		The current game level.
	 */
	public void tick(Level level)
	{
		if(level == null) return;
		
		++suppressed; // Ensure at least one tick is performed
		
		var gt = level.getGameTime();
		if(gt != gameTime)
		{
			gameTime = gt;
			tick.tick(suppressed);
			suppressed = 0; // reset suppressed tick count
		}
	}
	
	/**
	 * Normalized version of {@link org.zeith.hammerlib.tiles.TileSyncableTickable#atTickRate(int)}.
	 *
	 * @param rate
	 * 		The desired tick rate.
	 *
	 * @return true if the current game time is at the specified tick rate, false otherwise.
	 */
	public boolean atTickRate(int rate)
	{
		return gameTime % rate == 0L;
	}
	
	/**
	 * Creates a new instance of the NormalizedTicker class.
	 *
	 * @param tick
	 * 		The tick function to be normalized.
	 *
	 * @return A new instance of the NormalizedTicker class.
	 */
	public static NormalizedTicker create(NormalizedTickFunction tick)
	{
		return new NormalizedTicker(tick);
	}
	
	/**
	 * The NormalizedTickFunction interface defines the contract for a normalized tick function.
	 */
	@FunctionalInterface
	public interface NormalizedTickFunction
	{
		/**
		 * The tick method is called once per game tick and is passed the number of suppressed ticks.
		 *
		 * @param suppressedTicks
		 * 		The number of suppressed ticks.
		 */
		void tick(@Range(from = 1, to = Integer.MAX_VALUE) int suppressedTicks);
	}
}