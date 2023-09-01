package org.zeith.hammerlib.net.packets;

import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.*;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;

public class RequestPropertiesPacket
		implements IPacket
{
	IObjectSource<?> source;
	
	public RequestPropertiesPacket(IObjectSource<?> source)
	{
		this.source = source;
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeNbt(IObjectSource.writeSource(source));
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		source = IObjectSource.readSource(buf.readNbt()).orElse(null);
	}
	
	@Override
	public void serverExecute(PacketContext ctx)
	{
		var lvl = ctx.getSender().getLevel();
		if(source != null)
		{
			var props = source.get(IBasePropertyHolder.class, lvl).orElse(null);
			if(props != null)
			{
				var pd = props.getProperties();
				ctx.withReply(pd.createGlobalUpdate());
			}
		}
	}
}