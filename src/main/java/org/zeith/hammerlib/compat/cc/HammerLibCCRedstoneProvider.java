package org.zeith.hammerlib.compat.cc;

import dan200.computercraft.api.redstone.BundledRedstoneProvider;
import net.minecraft.core.*;
import net.minecraft.world.level.Level;
import org.zeith.api.blocks.redstone.*;


public class HammerLibCCRedstoneProvider
		implements BundledRedstoneProvider
{
	@Override
	public int getBundledRedstoneOutput(Level level, BlockPos pos, Direction side)
	{
		var cap = level.getCapability(RedstoneBundleCapability.BLOCK, pos, side);
		if(cap != null)
			return IRedstoneBundleAccessor.getSerializedBundleSignal(cap);
		return -1;
	}
}