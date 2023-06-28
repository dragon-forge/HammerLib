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
		var be = level.getBlockEntity(pos);
		if(be == null) return 0;
		var cap = be.getCapability(IRedstoneBundle.REDSTONE_BUNDLE(), side).resolve().orElse(null);
		if(cap != null) return IRedstoneBundleAccessor.getSerializedBundleSignal(cap);
		return -1;
	}
}