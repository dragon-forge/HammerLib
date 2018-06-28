package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import com.zeitheron.hammercore.internal.init.BlocksHC;
import com.zeitheron.hammercore.utils.WorldUtil;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Some useful utilities for Mutlipart API
 */
public final class MultipartAPI
{
	private MultipartAPI()
	{
	}
	
	public static TileMultipart getOrPlaceMultipart(World world, BlockPos pos)
	{
		if(world != null && pos != null && world.isBlockLoaded(pos))
		{
			TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
			
			// Fix, we don't want any unwanted replacements going on
			boolean replaceable = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
			if(tmp == null && replaceable)
				world.setBlockState(pos, BlocksHC.MULTIPART.getDefaultState(), 11);
			else if(!replaceable)
				return tmp;
			
			tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
			if(tmp == null)
				world.setTileEntity(pos, tmp = new TileMultipart());
			return tmp;
		}
		return null;
	}
}