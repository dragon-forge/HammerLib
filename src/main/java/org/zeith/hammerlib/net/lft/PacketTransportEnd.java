package org.zeith.hammerlib.net.lft;

import net.minecraft.nbt.CompoundNBT;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PacketTransportEnd
		implements INBTPacket
{
	public String id;

	public PacketTransportEnd(String id)
	{
		this.id = id;
	}

	@Override
	public void write(CompoundNBT nbt)
	{
		nbt.putString("i", id);
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		id = nbt.getString("i");
	}

	@Override
	public void execute(PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(ctx.getSide(), id);
		if(s != null) s.end(ctx);
	}
}