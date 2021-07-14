package org.zeith.api.wrench;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.common.ToolType;

public interface IWrenchItem
{
	ToolType WRENCH_TOOL_TYPE = ToolType.get("wrench");

	boolean canWrench(ItemStack stack);

	void onWrenchUsed(ItemUseContext context);
}