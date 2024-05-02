package org.zeith.hammerlib.core.test;

import net.minecraft.core.GlobalPos;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.ExposedToLevelAction;
import org.zeith.hammerlib.util.java.reflection.SerializableMethodHandle;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

public class MethodHandleTest
{
	public static SerializableMethodHandle makePerformTest(GlobalPos pos)
	{
		return SerializableMethodHandle.create(MethodHandleTest.class, "performTest", null, pos);
	}
	
	@ExposedToLevelAction
	public static void performTest(GlobalPos pos)
	{
		var level = LogicalSidePredictor.getLevel(pos.dimension());
		var bpos = pos.pos();
		HammerLib.LOG.error("MethodHandleTest.performTest called from {} in level {}", bpos, level);
	}
}