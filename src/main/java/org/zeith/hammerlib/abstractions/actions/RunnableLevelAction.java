package org.zeith.hammerlib.abstractions.actions;

import net.minecraft.server.level.ServerLevel;
import org.zeith.hammerlib.abstractions.actions.impl.DelayedLevelAction;

/**
 * This class represents a single work unit that can be performed in the future by the server.
 * <p>
 * It can be either on the next tick, continuous over a period of time, or delayed.
 * <p>
 * The {@link ILevelActionType} must be a registered type that is used to deserialize instances of this class from NBT.
 */
public abstract class RunnableLevelAction
		extends LevelAction
{
	public RunnableLevelAction(ILevelActionType type)
	{
		super(type);
	}
	
	/**
	 * This method gets called on next server tick after task has been enqueued.
	 */
	public abstract void run(ServerLevel level);
	
	/**
	 * Enqueues this action into the server level to be performed on next server tick.
	 */
	public void enqueue(ServerLevel level)
	{
		WorldSavedActions.enqueue(level, this);
	}
	
	/**
	 * Adds a delay before this action will be called after enqueuing.
	 * <p>
	 * If this action is {@link ContinuousLevelAction}, then this action will keep firing after the delay is up until it's done.
	 *
	 * @return A new action that can be enqueued and will create an N-tick delay before this action will fire.
	 */
	public DelayedLevelAction delay(int delay)
	{
		return new DelayedLevelAction(delay, this);
	}
}