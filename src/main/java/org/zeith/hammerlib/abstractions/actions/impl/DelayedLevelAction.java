package org.zeith.hammerlib.abstractions.actions.impl;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.abstractions.actions.*;
import org.zeith.hammerlib.core.init.LevelActionTypesHL;
import org.zeith.hammerlib.util.java.Cast;

/**
 * This is a wrapper action that will fire its sub-action after a given delay.
 * <p>
 * Its {@link #delay(int)} method returns itself instead of creating a new instance.
 */
public class DelayedLevelAction
		extends ContinuousLevelAction
{
	protected final RunnableLevelAction action;
	protected int delay;
	
	public DelayedLevelAction(int delay, RunnableLevelAction action)
	{
		this(LevelActionTypesHL.DELAYED_TYPE, delay, action);
	}
	
	public DelayedLevelAction(ILevelActionType type, int delay, RunnableLevelAction action)
	{
		super(type);
		this.delay = delay;
		this.action = action;
	}
	
	public DelayedLevelAction(ILevelActionType type, Level level, CompoundTag tag)
	{
		this(type,
				tag.getInt("Delay"),
				read(level, tag.getCompound("Run"))
						.map(Cast.convertTo(RunnableLevelAction.class))
						.orElse(null)
		);
	}
	
	@Override
	public DelayedLevelAction delay(int delay)
	{
		this.delay += delay;
		return this;
	}
	
	@Override
	public boolean isDone()
	{
		return action == null || (delay < 0 && (!(action instanceof ContinuousLevelAction c) || c.isDone()));
	}
	
	@Override
	public void run(ServerLevel level)
	{
		--delay;
		if(delay < 0) action.run(level);
	}
	
	@Override
	public CompoundTag write(Level level)
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("Delay", delay);
		tag.put("Run", write(level, action));
		return tag;
	}
	
	public static class DelayedType
			implements ILevelActionType
	{
		@Override
		public LevelAction read(Level level, CompoundTag tag)
		{
			return new DelayedLevelAction(this, level, tag);
		}
	}
}