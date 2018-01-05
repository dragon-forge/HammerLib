package com.pengu.hammercore.common.items.debug;

import com.pengu.hammercore.raytracer.RayTracer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public final class ItemRayTracer extends Item
{
	public ItemRayTracer()
	{
		setUnlocalizedName("ray_tracer");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		RayTraceResult result = RayTracer.retrace(playerIn, 128, true);
		if(playerIn.isSneaking() || !worldIn.isRemote)
			playerIn.sendMessage(new TextComponentString("[" + (worldIn.isRemote ? "CLIENT" : "SERVER") + "] " + result));
		return super.onItemRightClick(worldIn, playerIn, hand);
	}
}