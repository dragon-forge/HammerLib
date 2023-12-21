package org.zeith.hammerlib.compat.cc;

import dan200.computercraft.shared.common.IBundledRedstoneBlock;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Range;
import org.zeith.api.blocks.redstone.*;

public class ComputerBundleProvider
		implements IRedstoneBundle
{
	public final AbstractComputerBlockEntity computer;
	public final Direction side;
	
	public ComputerBundleProvider(AbstractComputerBlockEntity computer, Direction side)
	{
		this.computer = computer;
		this.side = side;
	}
	
	@Override
	public boolean isConnected()
	{
		if(computer.getBlockState().getBlock() instanceof IBundledRedstoneBlock blk)
			return blk.getBundledRedstoneConnectivity(computer.getLevel(), computer.getBlockPos(), side);
		return false;
	}
	
	@Override
	public @Range(from = 0, to = 65535) int getSerializedBundleSignal()
	{
		if(computer.getBlockState().getBlock() instanceof IBundledRedstoneBlock blk)
			return blk.getBundledRedstoneOutput(computer.getLevel(), computer.getBlockPos(), side);
		
		return 0;
	}
	
	@Override
	public boolean hasSignal(MCColor color)
	{
		if(computer.getBlockState().getBlock() instanceof IBundledRedstoneBlock blk)
		{
			var redstone = blk.getBundledRedstoneOutput(computer.getLevel(), computer.getBlockPos(), side);
			return IRedstoneBundleAccessor.hasSerialized(redstone, color);
		}
		
		return false;
	}
	
	@Override
	public boolean setSignal(MCColor color, boolean signal)
	{
		return false;
	}
	
	@Override
	public boolean setSerializedBundleSignal(@Range(from = 0, to = 65535) int serialized)
	{
		return false;
	}
}