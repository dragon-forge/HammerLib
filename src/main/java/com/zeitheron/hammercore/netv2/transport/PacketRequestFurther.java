package com.zeitheron.hammercore.netv2.transport;

import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRequestFurther implements IV2Packet
{
	static
	{
		HCV2Net.INSTANCE.handle(PacketRequestFurther.class, () -> new PacketRequestFurther(null, false));
	}
	
	public String id;
	public boolean state;
	
	public PacketRequestFurther(String id, boolean state)
	{
		this.state = state;
		this.id = id;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("s", state);
		nbt.setString("i", id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		state = nbt.getBoolean("s");
		id = nbt.getString("i");
	}
	
	@Override
	public IV2Packet execute(Side side, PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(side, id);
		
		if(s != null)
		{
			if(state)
				if(!s.pending.isEmpty())
					return new PacketTransport(id, s.pending.remove(0));
				else
				{
					s.end();
					return new PacketTransportEnd(id);
				}
			else
				s.end();
		}
		
		return null;
	}
}