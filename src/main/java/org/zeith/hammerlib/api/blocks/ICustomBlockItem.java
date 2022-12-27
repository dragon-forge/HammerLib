package org.zeith.hammerlib.api.blocks;

import net.minecraft.world.item.BlockItem;

/**
 * Interface to be implemented by blocks that have custom {@link BlockItem}s.
 */
public interface ICustomBlockItem
{
	/**
	 * Creates and returns the custom {@link BlockItem} for this block.
	 *
	 * @return the custom {@link BlockItem} for this block
	 */
	BlockItem createBlockItem();
}