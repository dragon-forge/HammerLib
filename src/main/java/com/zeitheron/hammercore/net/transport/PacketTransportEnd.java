package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportEnd implements IPacket
{
	static
	{
		IPacket.handle(PacketTransportEnd.class, () -> new PacketTransportEnd(null));
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
	public IPacket execute(Side side, PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(side, id);
		if(s != null)
			s.end();
		
		return null;
	}
}