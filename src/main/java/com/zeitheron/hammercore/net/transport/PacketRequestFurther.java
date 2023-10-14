package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRequestFurther implements IPacket
{
	static
	{
		HCNet.INSTANCE.handle(PacketRequestFurther.class, PacketRequestFurther::new);
	}
	
	public String id;
	public boolean state;
	
	public PacketRequestFurther(String id, boolean state)
	{
		this.state = state;
		this.id = id;
	}
	
	public PacketRequestFurther()
	{
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
	public IPacket execute(Side side, PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(side, id);
		
		if(s != null)
		{
			if(state)
				if(!s.pending.isEmpty())
					return new PacketTransport(id, s.pending.remove(0));
				else
				{
					s.end(side, ctx);
					return new PacketTransportEnd(id);
				}
			else
				s.end(side, ctx);
		}
		
		return null;
	}
}