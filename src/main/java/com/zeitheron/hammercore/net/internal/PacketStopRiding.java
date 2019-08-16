package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.MainThreaded;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MainThreaded
public class PacketStopRiding implements IPacket
{
	public int a;
	
	static
	{
		IPacket.handle(PacketStopRiding.class, PacketStopRiding::new);
	}
	
	public PacketStopRiding(Entity rider)
	{
		this.a = rider.getEntityId();
	}
	
	public PacketStopRiding()
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void executeOnClient2(PacketContext net)
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