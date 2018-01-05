package com.pengu.hammercore.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public interface iWrenchItem
{
	public boolean canWrench(ItemStack stack);
	
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand);
}