package com.pengu.hammercore.net.pkt;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSetBiome implements iPacket, iPacketListener<PacketSetBiome, iPacket>
{
	int x, z, id;
	byte biome;
	
	public PacketSetBiome(int x, int z, int id, byte biome)
	{
		this.x = x;
		this.z = z;
		this.id = id;
		this.biome = biome;
	}
	
	public PacketSetBiome()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setIntArray("d", new int[] { x, z, id, biome });
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int[] d = nbt.getIntArray("d");
		x = d[0];
		z = d[1];
		id = d[2];
		biome = (byte) d[3];
	}
	
	@Override
	public iPacket onArrived(PacketSetBiome packet, MessageContext context)
	{
		return null;
	}
}