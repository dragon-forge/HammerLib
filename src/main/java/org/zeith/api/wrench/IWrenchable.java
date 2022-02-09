package org.zeith.api.wrench;

import net.minecraft.world.item.context.UseOnContext;

/**
 * Universal interface to be applied to both Block and TileEntity
 */
public interface IWrenchable
{
	/**
	 * Called when player clicks with {@link IWrenchItem} if
	 * {@link IWrenchItem#canWrench(net.minecraft.world.item.ItemStack)} returns true.
	 *
	 * @param context Context of the wrench action.
	 * @return If the wrench operation was successful and had any result.
	 */
	boolean onWrenchUsed(UseOnContext context);
}