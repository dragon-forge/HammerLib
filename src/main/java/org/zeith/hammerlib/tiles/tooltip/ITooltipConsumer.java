package org.zeith.hammerlib.tiles.tooltip;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public interface ITooltipConsumer
{
	Vec3 getHitVec();
	
	Direction getSideHit();
	
	void addLine(Component text);
	
	void addItem(ItemStack stack);
	
	void addBar(ProgressBar bar);
}