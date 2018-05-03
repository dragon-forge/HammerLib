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

public class PacketStartedRiding implements iPacket, iPacketListener<PacketStartedRiding, iPacket>
{
	public int a, b;
	
	public PacketStartedRiding(Entity rider, Entity ridden)
	{
		this.a = rider.getEntityId();
		this.b = ridden.getEntityId();
	}
	
	public PacketStartedRiding()
	{
	}
	
	@Override
	public iPacket onArrived(PacketStartedRiding packet, MessageContext context)
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
		Entity ridden = wor.getEntityByID(b);
		if(rider != null && ridden != null)
			rider.startRiding(ridden, true);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("a", a);
		nbt.setInteger("b", b);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		a = nbt.getInteger("a");
		b = nbt.getInteger("b");
	}
}