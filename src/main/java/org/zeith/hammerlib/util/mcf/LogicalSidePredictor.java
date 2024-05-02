package org.zeith.hammerlib.util.mcf;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class LogicalSidePredictor
{
	public static LogicalSide getCurrentLogicalSide(LevelReader level)
	{
		return level.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER;
	}
	
	public static LogicalSide getCurrentLogicalSide()
	{
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? LogicalSide.SERVER : LogicalSide.CLIENT;
	}
	
	public static ServerLevel getLevel(ResourceKey<Level> level)
	{
		var server = ServerLifecycleHooks.getCurrentServer();
		if(server == null) return null;
		return server.getLevel(level);
	}
}