package com.zeitheron.hammercore.client.render.item;

import com.zeitheron.hammercore.utils.InterItemStack;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityItemStackRendererHC extends TileEntityItemStackRenderer
{
	private final TileEntityItemStackRenderer parent;
	
	{
		parent = instance;
		instance = this;
	}
	
	@Override
	public void renderByItem(ItemStack itemStackIn)
	{
		if(itemStackIn.isEmpty() || !ItemRenderingHandler.INSTANCE.canRender(itemStackIn.getItem()))
			parent.renderByItem(itemStackIn);
	}
}