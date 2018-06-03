package com.zeitheron.hammercore.netv2.internal;

import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.IV2Packet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

public class V2PacketPing implements IV2Packet
{
	static
	{
		HCV2Net.INSTANCE.handle(V2PacketPing.class, () -> new V2PacketPing(0L));
	}
	
	public V2PacketPing(long start)
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
	public IV2Packet executeOnClient()
	{
		HammerCoreClient.ping = System.currentTimeMillis() - create;
		return null;
	}
	
	@Override
	public IV2Packet executeOnServer()
	{
		return this;
	}
}