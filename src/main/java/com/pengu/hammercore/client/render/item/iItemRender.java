package com.pengu.hammercore.client.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface iItemRender
{
	void renderItem(ItemStack item);
}