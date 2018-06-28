package com.zeitheron.hammercore.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class NetUtil
{
	public static final int BYTE_ARRAY_START = 0x01, BYTE_ARRAY_END = 0x02, BLOCK_POS_START = 0x03, BLOCK_POS_END = 0x04;
	
	/**
	 * Attempts to write byte array to buffer
	 **/
	public static void writeByteArray(byte[] b, ByteBuf buf)
	{
		if(b == null || buf == null)
			return;
		buf.writeInt(BYTE_ARRAY_START);
		buf.writeInt(b.length);
		buf.writeBytes(b);
		buf.writeInt(BYTE_ARRAY_END);
	}
	
	/**
	 * Attempts to read byte array from buffer
	 * 
	 * @return primitive {@link byte[]}, or null, if packet is corrupted.
	 **/
	public static byte[] readByteArray(ByteBuf buf)
	{
		if(buf.readInt() == BYTE_ARRAY_START)
		{
			byte[] bytes = new byte[buf.readInt()];
			buf.readBytes(bytes);
			return buf.readInt() == BYTE_ARRAY_END ? bytes : null;
		}
		
		return null;
	}
	
	public static NBTTagCompound readCompound(ByteBuf buf)
	{
		return ByteBufUtils.readTag(buf);
	}
	
	public static void writeCompound(ByteBuf buf, NBTTagCompound nbt)
	{
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static ItemStack readStack(ByteBuf buf)
	{
		return ByteBufUtils.readItemStack(buf);
	}
	
	public static void writeStack(ByteBuf buf, ItemStack stack)
	{
		ByteBufUtils.writeItemStack(buf, stack);
	}
	
	public static void writeBlockPos(BlockPos pos, ByteBuf buf)
	{
		if(pos == null)
			return;
		buf.writeInt(BLOCK_POS_START);
		buf.writeLong(pos.toLong());
		buf.writeInt(BLOCK_POS_END);
	}
	
	public static BlockPos loadBlockPos(ByteBuf buf)
	{
		if(buf.readInt() != BLOCK_POS_START)
			return null;
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		return buf.readInt() == BLOCK_POS_END ? pos : null;
	}
}