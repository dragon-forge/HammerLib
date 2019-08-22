package com.zeitheron.hammercore.api.inconnect;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockConnectable
{
	/**
	 * Please represent texture path to your texture. <br>
	 * Example: <code>new ResourceLocation("minecraft", "blocks/bedrock")</code>
	 */
	ResourceLocation getTxMap();
	
	AxisAlignedBB getBlockShape(IBlockAccess world, BlockPos pos, IBlockState state);
}