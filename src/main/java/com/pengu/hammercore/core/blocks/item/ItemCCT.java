package com.pengu.hammercore.core.blocks.item;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.client.SidedKeyHelper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCCT extends ItemBlock
{
	public ItemCCT(Block block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		byte mode = 0;
		if(HammerCore.renderProxy.getClientPlayer() == null) /* This may be
		                                                      * called by JEI or
		                                                      * other mods while
		                                                      * sorting items in
		                                                      * item list */
			mode = 2;
		else if(SidedKeyHelper.cIsKeyPressed(SidedKeyHelper.KEY_LSHIFT) || SidedKeyHelper.cIsKeyPressed(SidedKeyHelper.KEY_RSHIFT))
			mode = 1;
		return super.getUnlocalizedName(stack) + "_" + mode;
	}
}