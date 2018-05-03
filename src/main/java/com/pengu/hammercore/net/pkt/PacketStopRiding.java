package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketStopRiding implements iPacket, iPacketListener<PacketStopRiding, iPacket>
{
	public int a;
	
	public PacketStopRiding(Entity rider)
	{
		this.a = rider.getEntityId();
	}
	
	public PacketStopRiding()
	{
	}
	
	@Override
	public iPacket onArrived(PacketStopRiding packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		World wor = Minecraft.getMinecraft().world;
		Entity rider = wor.getEntityByID(a);
		if(rider != null)
			rider.dismountRidingEntity();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("a", a);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		a = nbt.getInteger("a");
	}
}