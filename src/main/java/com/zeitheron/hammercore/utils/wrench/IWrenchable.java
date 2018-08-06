package com.zeitheron.hammercore.utils.wrench;

import com.zeitheron.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

/**
 * Universal interface to be applied to both Block and TileEntity
 */
public interface IWrenchable
{
	/**
	 * Called when player clicks with {@link IWrenchItem} if
	 * {@link IWrenchItem#canWrench(net.minecraft.item.ItemStack)} returns true.
	 * 
	 * @param loc
	 *            The location of the action
	 * @param player
	 *            The causer of the action
	 * @param hand
	 *            The hand of the player
	 * @return If the wrench operation was successful
	 */
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand);
}