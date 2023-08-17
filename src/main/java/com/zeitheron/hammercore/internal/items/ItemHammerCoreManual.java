package com.zeitheron.hammercore.internal.items;

import com.zeitheron.hammercore.HammerCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemHammerCoreManual
		extends Item
{
	public ItemHammerCoreManual()
	{
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		HammerCore.bookProxy.openNewBook();
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}