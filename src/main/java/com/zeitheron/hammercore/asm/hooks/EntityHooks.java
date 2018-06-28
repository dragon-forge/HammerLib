package com.zeitheron.hammercore.asm.hooks;

import com.zeitheron.hammercore.event.FoodEatenEvent;
import com.zeitheron.hammercore.event.ItemUseFinishEvent;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class EntityHooks
{
	public static void onItemUseFinish(EntityLivingBase base)
	{
		ItemStack activeItem = base.getActiveItemStack();
		
		if(!activeItem.isEmpty() && activeItem.getItem() instanceof ItemFood)
		{
			FoodEatenEvent fee = new FoodEatenEvent(base, activeItem);
			MinecraftForge.EVENT_BUS.post(fee);
		} else
		{
			ItemUseFinishEvent iufe = new ItemUseFinishEvent(base, activeItem);
			MinecraftForge.EVENT_BUS.post(iufe);
		}
	}
}