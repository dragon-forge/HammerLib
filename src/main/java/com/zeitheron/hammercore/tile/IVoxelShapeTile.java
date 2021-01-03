package com.zeitheron.hammercore.tile;

import com.zeitheron.hammercore.utils.math.VoxelShape;

public interface IVoxelShapeTile
{
	/**
	 * Gets the entire list of all lines to be rendered on block highlight.
	 */
	VoxelShape getHighlightedLines();
}