package com.pengu.hammercore.client.render.item;

import com.pengu.hammercore.common.InterItemStack;

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
		if(InterItemStack.isStackNull(itemStackIn))
			return;
		Item item = itemStackIn.getItem();
		
		if(!ItemRenderingHandler.INSTANCE.canRender(item))
			parent.renderByItem(itemStackIn);
	}
}