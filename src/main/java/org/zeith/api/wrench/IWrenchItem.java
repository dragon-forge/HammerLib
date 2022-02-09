package org.zeith.api.wrench;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public interface IWrenchItem
{
	boolean canWrench(ItemStack stack);

	void onWrenchUsed(UseOnContext context);
}