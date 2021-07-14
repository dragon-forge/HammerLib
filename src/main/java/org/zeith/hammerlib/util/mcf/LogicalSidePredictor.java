package org.zeith.hammerlib.util.mcf;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;

public class LogicalSidePredictor
{
	public static LogicalSide getCurrentLogicalSide()
	{
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? LogicalSide.SERVER : LogicalSide.CLIENT;
	}
}