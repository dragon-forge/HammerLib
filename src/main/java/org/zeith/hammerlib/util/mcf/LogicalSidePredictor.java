package org.zeith.hammerlib.util.mcf;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.relauncher.Side;

public class LogicalSidePredictor
{
	public static Side getCurrentLogicalSide(World level)
	{
		return level.isRemote ? Side.CLIENT : Side.SERVER;
	}
	
	public static Side getCurrentLogicalSide()
	{
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? Side.SERVER : Side.CLIENT;
	}
}