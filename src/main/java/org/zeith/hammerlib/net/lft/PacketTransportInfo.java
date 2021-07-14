package org.zeith.hammerlib.net.lft;

import net.minecraft.nbt.CompoundNBT;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PacketTransportInfo
		implements INBTPacket
{
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
	public void write(CompoundNBT nbt)
	{
		nbt.putString("i", id);
		nbt.putString("c", clas);
		nbt.putInt("l", length);
	}

	@Override
	public void read(CompoundNBT nbt)
	{
		id = nbt.getString("i");
		clas = nbt.getString("c");
		length = nbt.getInt("l");
	}

	@Override
	public void execute(PacketContext ctx)
	{
		try
		{
			Class<? extends ITransportAcceptor> cl = Class.forName(clas).asSubclass(ITransportAcceptor.class);
			ITransportAcceptor it = cl.getDeclaredConstructor().newInstance();
			it.setInitialContext(ctx);
			new TransportSession(id, cl, null, it, length);
			ctx.withReply(new PacketRequestFurther(id, true));
			return;
		} catch(Throwable err)
		{
			err.printStackTrace();
		}
		ctx.withReply(new PacketRequestFurther(id, false));
	}
}