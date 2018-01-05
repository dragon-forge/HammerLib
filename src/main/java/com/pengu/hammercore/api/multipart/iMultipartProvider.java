package com.pengu.hammercore.api.multipart;

import com.pengu.hammercore.common.blocks.multipart.TileMultipart;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Provides {@link MultipartSignature}. Used for blocks.
 */
public interface iMultipartProvider
{
	MultipartSignature createSignature(int signatureIndex, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);
	
	default boolean canPlaceInto(TileMultipart multipart, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return true;
	}
}