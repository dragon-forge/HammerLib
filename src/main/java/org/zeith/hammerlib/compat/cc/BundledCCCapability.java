package org.zeith.hammerlib.compat.cc;

import dan200.computercraft.shared.common.IBundledRedstoneBlock;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.*;
import org.zeith.api.blocks.redstone.*;

public class BundledCCCapability
		implements ICapabilityProvider
{
	public final AbstractComputerBlockEntity computer;
	
	public BundledCCCapability(AbstractComputerBlockEntity computer)
	{
		this.computer = computer;
	}
	
	public final LazyOptional<IRedstoneBundle>[] sidedBundles = Util.make(new LazyOptional[6], s ->
	{
		for(var side : Direction.values())
			s[side.ordinal()] = LazyOptional.of(() -> new ComputerBundleProvider(side));
	});
	
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == IRedstoneBundle.REDSTONE_BUNDLE() && side != null)
			return sidedBundles[side.ordinal()].cast();
		return LazyOptional.empty();
	}
	
	public class ComputerBundleProvider
			implements IRedstoneBundle
	{
		public final Direction side;
		
		public ComputerBundleProvider(Direction side)
		{
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
}