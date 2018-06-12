package com.zeitheron.hammercore.netv2.transport;

import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportEnd implements IV2Packet
{
	static
	{
		IV2Packet.handle(PacketTransportEnd.class, () -> new PacketTransportEnd(null));
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
	public IV2Packet execute(Side side, PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(side, id);
		if(s != null)
			s.end();
		
		return null;
	}
}