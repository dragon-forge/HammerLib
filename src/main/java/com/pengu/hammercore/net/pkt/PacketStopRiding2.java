package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketStopRiding2 implements iPacket, iPacketListener<PacketStopRiding2, iPacket>
{
	public int a, b;
	
	public PacketStopRiding2(Entity rider, Entity ridden)
	{
		this.a = rider.getEntityId();
		this.b = ridden.getEntityId();
	}
	
	public PacketStopRiding2()
	{
	}
	
	@Override
	public iPacket onArrived(PacketStopRiding2 packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			World wor = context.getServerHandler().player.world;
			Entity rider = wor.getEntityByID(a);
			Entity ridden = wor.getEntityByID(b);
			if(rider != null && ridden != null)
			{
				rider.dismountRidingEntity();
				if(ridden instanceof EntityPlayerMP)
					HCNetwork.manager.sendTo(new PacketStopRiding(rider), (EntityPlayerMP) ridden);
			}
		}
		return null;
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