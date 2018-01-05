package com.pengu.hammercore.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface iTileDroppable
{
	public void createDrop(EntityPlayer player, World world, BlockPos pos);
}