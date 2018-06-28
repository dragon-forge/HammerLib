package com.zeitheron.hammercore.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class LyingItemPickedUpEvent extends PlayerEvent
{
	public final BlockPos pickPos;
	public ItemStack drop;
	public final boolean natural;
	
	public LyingItemPickedUpEvent(EntityPlayer player, BlockPos pickPos, ItemStack pickedStack, boolean natural)
	{
		super(player);
		this.pickPos = pickPos;
		this.drop = pickedStack;
		this.natural = natural;
	}
}