package com.zeitheron.hammercore.utils.wrench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public interface IWrenchItem
{
	public boolean canWrench(ItemStack stack);
	
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand);
}