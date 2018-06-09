package com.zeitheron.hammercore.netv2.transport;

import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.IV2Packet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketTransport implements IV2Packet
{
	static
	{
		HCV2Net.INSTANCE.handle(PacketTransport.class, () -> new PacketTransport(null, null));
	}
	
	public String id;
	public byte[] data;
	
	public PacketTransport(String id, byte[] data)
	{
		this.id = id;
		this.data = data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getString("i");
		if(nbt.hasKey("r", NBT.TAG_BYTE_ARRAY))
			data = nbt.getByteArray("r");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("i", id);
		if(data != null)
			nbt.setByteArray("r", data);
	}
	
	@Override
	public IV2Packet execute(Side side)
	{
		TransportSession s = NetTransport.getSession(side, id);
		if(s != null && s.pos != null && data != null)
		{
			s.accept(data);
			return new PacketRequestFurther(id, true);
		}
		return new PacketRequestFurther(id, false);
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