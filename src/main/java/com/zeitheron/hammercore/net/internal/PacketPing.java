package com.zeitheron.hammercore.net.internal;

import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

public class PacketPing implements IPacket
{
	static
	{
		IPacket.handle(PacketPing.class, () -> new PacketPing(0L));
	}
	
	public PacketPing(long start)
	{
		create = start;
	}
	
	public long create;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		create = nbt.getLong("Create");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("Create", create);
	}
	
	@Override
	public IPacket executeOnClient(PacketContext ctx)
	{
		HammerCoreClient.ping = System.currentTimeMillis() - create;
		return null;
	}
	
	@Override
	public IPacket executeOnServer(PacketContext ctx)
	{
		return this;
	}
}