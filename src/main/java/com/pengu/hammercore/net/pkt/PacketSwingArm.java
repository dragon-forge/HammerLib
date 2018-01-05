package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSwingArm implements iPacket, iPacketListener<PacketSwingArm, iPacket>
{
	public EnumHand hand;
	
	public PacketSwingArm(EnumHand hand)
	{
		this.hand = hand;
	}
	
	public PacketSwingArm()
	{
	}
	
	@Override
	public iPacket onArrived(PacketSwingArm packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.run();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void run()
	{
		Minecraft.getMinecraft().player.swingArm(hand);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p1", hand.ordinal());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		hand = EnumHand.values()[nbt.getInteger("p1")];
	}
}