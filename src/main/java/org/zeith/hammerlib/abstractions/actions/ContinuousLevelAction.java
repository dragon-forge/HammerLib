package org.zeith.hammerlib.abstractions.actions;

import net.minecraft.server.level.ServerLevel;

/**
 * Represents a configured instance of a continuous level action.
 * Whenever enqueued onto a {@link ServerLevel}, this will keep on executing {@link #run(ServerLevel)} until {@link #isDone()} returns true.
 */
public abstract class ContinuousLevelAction
		extends RunnableLevelAction
{
	public ContinuousLevelAction(ILevelActionType type)
	{
		super(type);
	}
	
	/**
	 * Called immediately after {@link #run(ServerLevel)} to check if the actions has been completed after the update.
	 *
	 * @return true if this action is done after the call to {@link #run(ServerLevel)}.
	 */
	public abstract boolean isDone();
}