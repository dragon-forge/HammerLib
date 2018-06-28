package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransport implements IPacket
{
	static
	{
		IPacket.handle(PacketTransport.class, () -> new PacketTransport(null, null));
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
	public IPacket execute(Side side, PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(side, id);
		if(s != null && s.pos != null && data != null)
		{
			s.accept(data);
			return new PacketRequestFurther(id, true);
		}
		return new PacketRequestFurther(id, false);
	}
}