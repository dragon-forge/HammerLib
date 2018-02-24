package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncMouseStack implements iPacket, iPacketListener<PacketSyncMouseStack, iPacket>
{
	ItemStack mouse;
	
	public PacketSyncMouseStack(ItemStack stack)
	{
		this.mouse = stack;
	}
	
	public PacketSyncMouseStack()
	{
	}
	
	@Override
	public iPacket onArrived(PacketSyncMouseStack packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> mc.player.inventory.setItemStack(mouse));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		this.mouse.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.mouse = new ItemStack(nbt);
	}
}