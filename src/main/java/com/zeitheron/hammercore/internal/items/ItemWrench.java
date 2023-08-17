package com.zeitheron.hammercore.internal.items;

import com.zeitheron.hammercore.utils.wrench.IWrenchItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ItemWrench
		extends Item
		implements IWrenchItem
{
	public static void enable()
	{
		System.setProperty("hammercore.enableWrench", "true");
	}
	
	public ItemWrench()
	{
		setMaxStackSize(1);
	}
	
	@Override
	public boolean canWrench(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand)
	{
	}
}