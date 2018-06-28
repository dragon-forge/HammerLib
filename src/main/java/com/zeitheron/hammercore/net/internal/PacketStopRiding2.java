package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PacketStopRiding2 implements IPacket
{
	public int a, b;
	
	static
	{
		IPacket.handle(PacketStopRiding2.class, PacketStopRiding2::new);
	}
	
	public PacketStopRiding2(Entity rider, Entity ridden)
	{
		this.a = rider.getEntityId();
		this.b = ridden.getEntityId();
	}
	
	public PacketStopRiding2()
	{
	}
	
	@Override
	public IPacket executeOnServer(PacketContext net)
	{
		World wor = net.getSender().world;
		Entity rider = wor.getEntityByID(a);
		Entity ridden = wor.getEntityByID(b);
		if(rider != null && ridden != null)
		{
			rider.dismountRidingEntity();
			if(ridden instanceof EntityPlayerMP)
				HCNet.INSTANCE.sendTo(new PacketStopRiding(rider), (EntityPlayerMP) ridden);
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