package org.zeith.hammerlib.net.lft;

import net.minecraft.nbt.CompoundNBT;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PacketRequestFurther
		implements INBTPacket
{
	public String id;
	public boolean state;

	public PacketRequestFurther(String id, boolean state)
	{
		this.state = state;
		this.id = id;
	}

	@Override
	public void write(CompoundNBT nbt)
	{
		nbt.putBoolean("s", state);
		nbt.putString("i", id);
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		state = nbt.getBoolean("s");
		id = nbt.getString("i");
	}

	@Override
	public void execute(PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(ctx.getSide(), id);

		if(s != null)
		{
			if(state)
				if(!s.pending.isEmpty())
					ctx.withReply(new PacketTransport(id, s.pending.remove(0)));
				else
				{
					s.end(ctx);
					ctx.withReply(new PacketTransportEnd(id));
				}
			else
				s.end(ctx);
		}
	}
}