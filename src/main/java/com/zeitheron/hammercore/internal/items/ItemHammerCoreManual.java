package com.zeitheron.hammercore.internal.items;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemHammerCoreManual extends Item
{
	public ItemHammerCoreManual()
	{
		setTranslationKey("manual");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		HammerCore.bookProxy.openNewBook();
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}