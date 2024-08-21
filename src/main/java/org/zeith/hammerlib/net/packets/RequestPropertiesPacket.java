package org.zeith.hammerlib.net.packets;

import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.PacketContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import org.zeith.hammerlib.abstractions.sources.IObjectSource;
import org.zeith.hammerlib.net.properties.IBasePropertyHolder;
import org.zeith.hammerlib.net.properties.PropertyDispatcher;

import java.io.IOException;


public class RequestPropertiesPacket
		implements IPacket
{
	IObjectSource<?> source;
	
	public RequestPropertiesPacket(IObjectSource<?> source)
	{
		this.source = source;
	}
	
	public RequestPropertiesPacket()
	{
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeCompoundTag(IObjectSource.writeSource(source));
	}
	
	@Override
	public void read(PacketBuffer buf)
			throws IOException
	{
		source = IObjectSource.readSource(buf.readCompoundTag()).orElse(null);
	}
	
	@Override
	public void executeOnServer2(PacketContext ctx)
	{
		WorldServer lvl = ctx.getSender().getServerWorld();
		if(source != null)
		{
			IBasePropertyHolder props = source.get(IBasePropertyHolder.class, lvl).orElse(null);
			if(props != null)
			{
				PropertyDispatcher pd = props.getProperties();
				ctx.withReply(pd.createGlobalUpdate());
			}
		}
	}
}