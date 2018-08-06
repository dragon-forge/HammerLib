package com.zeitheron.hammercore.api.multipart;

import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Provides {@link MultipartSignature}. Used for blocks.
 */
public interface IMultipartProvider
{
	/**
	 * Creates an instance for multipart that will handle custom data.
	 */
	MultipartSignature createSignature(int signatureIndex, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);
	
	/**
	 * Peeks a {@link TileMultipart} if this multipart can be placed in it.
	 */
	default boolean canPlaceInto(TileMultipart multipart, ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return true;
	}
}