package com.zeitheron.hammercore.internal.blocks;

import net.minecraft.block.state.IBlockState;

public interface IWitherProofBlock
{
	default boolean isWitherproof(IBlockState state)
	{
		return true;
	}
}