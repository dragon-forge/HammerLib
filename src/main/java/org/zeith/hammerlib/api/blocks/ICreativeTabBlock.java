package org.zeith.hammerlib.api.blocks;

import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.items.CreativeTab;

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
	@NotNull
	CreativeTab getCreativeTab();
}