package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketStartedRiding implements IPacket
{
	public int a, b;
	
	static
	{
		IPacket.handle(PacketStartedRiding.class, PacketStartedRiding::new);
	}
	
	public PacketStartedRiding(Entity rider, Entity ridden)
	{
		this.a = rider.getEntityId();
		this.b = ridden.getEntityId();
	}
	
	public PacketStartedRiding()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket executeOnClient(PacketContext net)
	{
		World wor = Minecraft.getMinecraft().world;
		Entity rider = wor.getEntityByID(a);
		Entity ridden = wor.getEntityByID(b);
		if(rider != null && ridden != null)
			rider.startRiding(ridden, true);
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