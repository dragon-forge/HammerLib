package org.zeith.hammerlib.abstractions.actions.impl;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.abstractions.actions.ContinuousLevelAction;
import org.zeith.hammerlib.abstractions.actions.ILevelActionType;

import java.awt.*;

public class ExampleLevelAction
		extends ContinuousLevelAction
{
	public ExampleLevelAction(ILevelActionType type)
	{
		super(type);
	}
	
	@Override
	public boolean isDone()
	{
		return false;
	}
	
	@Override
	public void run(ServerLevel level)
	{
	
	}
	
	@Override
	public CompoundTag write(Level level)
	{
		CompoundTag nbt = new CompoundTag();
		
		return nbt;
	}
}