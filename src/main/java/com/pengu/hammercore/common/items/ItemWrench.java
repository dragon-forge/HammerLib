package com.pengu.hammercore.common.items;

import com.pengu.hammercore.client.ItemColorHelper;
import com.pengu.hammercore.client.render.Render3D;
import com.pengu.hammercore.common.iWrenchItem;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWrench extends Item implements iWrenchItem, iCustomEnchantColorItem
{
	public ItemWrench()
	{
		setUnlocalizedName("wrench");
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemIWrench)
			return Render3D.sneakTicks > 0;
		return false;
	}
	
	@Override
	public int getEnchantEffectColor(ItemStack stack)
	{
		int rgb = ItemColorHelper.DEFAULT_GLINT_COLOR;
		float alpha = Math.min(Render3D.sneakTicks, 20) / 20F;
		return ColorHelper.multiply(rgb, alpha);
	}
}