package com.zeitheron.hammercore.netv2.transport;

import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.IV2Packet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportEnd implements IV2Packet
{
	static
	{
		HCV2Net.INSTANCE.handle(PacketTransportEnd.class, () -> new PacketTransportEnd(null));
	}
	
	public String id;
	
	public PacketTransportEnd(String id)
	{
		this.id = id;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("i", id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getString("i");
	}
	
	@Override
	public IV2Packet execute(Side side)
	{
		TransportSession s = NetTransport.getSession(side, id);
		if(s != null)
			s.end();
		
		return null;
	}
	
	@Override
	public IV2Packet executeOnClient()
	{
		return execute(Side.CLIENT);
	}
	
	@Override
	public IV2Packet executeOnServer()
	{
		return execute(Side.SERVER);
	}
}