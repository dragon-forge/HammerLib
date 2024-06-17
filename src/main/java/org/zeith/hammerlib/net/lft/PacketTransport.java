package org.zeith.hammerlib.net.lft;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

public class PacketTransport
		implements IPacket
{
	public String id;
	public byte[] data;
	public RegistryAccess registry;
	
	public PacketTransport(String id, byte[] data)
	{
		this.id = id;
		this.data = data;
	}
	
	@Override
	public void write(RegistryFriendlyByteBuf buf)
	{
		buf.writeUtf(id);
		buf.writeByteArray(data);
	}
	
	@Override
	public void read(RegistryFriendlyByteBuf buf)
	{
		id = buf.readUtf();
		data = buf.readByteArray();
		this.registry = buf.registryAccess();
	}
	
	@Override
	public void execute(PacketContext ctx)
	{
		TransportSession s = NetTransport.getSession(ctx.getSide(), id);
		if(s != null && s.pos != null && data != null)
		{
			s.accept(registry, data);
			ctx.withReply(new PacketRequestFurther(id, true));
		} else
			ctx.withReply(new PacketRequestFurther(id, false));
	}
}