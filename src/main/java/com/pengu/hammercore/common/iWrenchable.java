package com.pengu.hammercore.common;

import com.pengu.hammercore.utils.WorldLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

/**
 * Universal interface to be applied to both Block and TileEntity
 */
public interface iWrenchable
{
	/**
	 * Called when player clicks with {@link iWrenchItem} if
	 * {@link iWrenchItem#canWrench(net.minecraft.item.ItemStack)} returns true.
	 */
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand);
}