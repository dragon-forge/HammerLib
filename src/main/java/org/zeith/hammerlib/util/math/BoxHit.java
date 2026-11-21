package org.zeith.hammerlib.util.math;

import net.minecraft.core.*;
import net.minecraft.world.phys.Vec3;

public record BoxHit(BlockPos pos, Vec3 hit, Direction hitFace)
{
	public Vec3 localHit()
	{
		return hit.subtract(pos.getX(), pos.getY(), pos.getZ());
	}
}