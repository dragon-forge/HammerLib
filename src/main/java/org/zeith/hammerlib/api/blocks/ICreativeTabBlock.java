package org.zeith.hammerlib.api.blocks;

import net.minecraft.world.item.CreativeModeTab;

/**
 * Interface for blocks that show in a creative mode tab.
 */
public interface ICreativeTabBlock
{
	/**
	 * Gets the creative mode tab for this block.
	 *
	 * @return Creative mode tab for this block.
	 */
	CreativeModeTab getCreativeTab();
}