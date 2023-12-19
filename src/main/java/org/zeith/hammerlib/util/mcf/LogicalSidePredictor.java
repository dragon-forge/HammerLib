package org.zeith.hammerlib.util.mcf;

import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.util.thread.SidedThreadGroups;

public class LogicalSidePredictor
{
	public static LogicalSide getCurrentLogicalSide()
	{
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? LogicalSide.SERVER : LogicalSide.CLIENT;
	}
}