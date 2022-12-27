package org.zeith.hammerlib.api.blocks;

import net.minecraft.world.item.Item;

/**
 * Interface for blocks to implement to provide custom item properties for their corresponding item.
 * Used in conjuction with @{@link org.zeith.hammerlib.annotations.SimplyRegister}, which works using {@link org.zeith.hammerlib.core.adapter.RegistryAdapter}.
 */
public interface IItemPropertySupplier
{
	/**
	 * Creates the item properties for this item.
	 *
	 * @param props
	 * 		The default item properties for the item.
	 *
	 * @return The modified item properties for the item.
	 */
	Item.Properties createItemProperties(Item.Properties props);
}