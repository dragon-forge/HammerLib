package com.zeitheron.hammercore.netv2.transport;

import com.zeitheron.hammercore.netv2.IV2Packet;
import com.zeitheron.hammercore.netv2.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportInfo implements IV2Packet
{
	static
	{
		IV2Packet.handle(PacketTransportInfo.class, () -> new PacketTransportInfo(null, null, 0));
	}
	
	public String id;
	public int length;
	public String clas;
	
	public PacketTransportInfo(String id, String clas, int size)
	{
		this.id = id;
		this.clas = clas;
		this.length = size;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("i", id);
		nbt.setString("c", clas);
		nbt.setInteger("l", length);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getString("i");
		clas = nbt.getString("c");
		length = nbt.getInteger("l");
	}
	
	@Override
	public IV2Packet execute(Side side, PacketContext ctx)
	{
		try
		{
			Class<? extends ITransportAcceptor> cl = Class.forName(clas).asSubclass(ITransportAcceptor.class);
			new TransportSession(id, cl, null, cl.getDeclaredConstructor().newInstance(), length);
			return new PacketRequestFurther(id, true);
		} catch(Throwable err)
		{
		}
		return new PacketRequestFurther(id, false);
	}
}