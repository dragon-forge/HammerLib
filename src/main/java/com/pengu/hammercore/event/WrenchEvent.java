package com.pengu.hammercore.event;

import javax.annotation.Nullable;

import com.pengu.hammercore.common.iWrenchItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when player clicks with {@link iWrenchItem} on block
 */
public class WrenchEvent extends Event
{
	public final EntityPlayer player;
	public final BlockPos pos;
	public final EnumHand hand;
	
	@Nullable
	public final EnumFacing facing;
	
	public WrenchEvent(EntityPlayer player, BlockPos pos, EnumHand hand, @Nullable EnumFacing facing)
	{
		this.player = player;
		this.pos = pos;
		this.hand = hand;
		this.facing = facing;
	}
}