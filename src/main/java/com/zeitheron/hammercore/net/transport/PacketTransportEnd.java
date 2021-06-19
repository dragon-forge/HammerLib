package com.zeitheron.hammercore.net.transport;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PacketTransportEnd
		implements IPacket
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
	public void executeOnServer2(PacketContext net)
	{
		TransportSession s = NetTransport.getSession(Side.SERVER, id);
		if(s != null) s.end(Side.SERVER, net);
	}

	@Override
	public void executeOnClient2(PacketContext net)
	{
		TransportSession s = NetTransport.getSession(Side.CLIENT, id);
		if(s != null) s.end(Side.CLIENT, net);
	}

	@Override
	public boolean executeOnMainThread()
	{
		Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
		TransportSession s = NetTransport.getSession(effectiveSide, id);
		return IPacket.super.executeOnMainThread() || (s != null && s.acceptori != null && s.acceptori.executeOnMainThread());
	}
}