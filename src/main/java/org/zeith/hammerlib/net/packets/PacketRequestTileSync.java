package org.zeith.hammerlib.net.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.net.*;

@MainThreaded
public class PacketRequestTileSync
		implements IPacket
{
	BlockPos pos;
	
	public PacketRequestTileSync()
	{
	}
	
	public PacketRequestTileSync(BlockPos pos)
	{
		this.pos = pos;
	}
	
	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}
	
	@Override
	public void read(FriendlyByteBuf buf)
	{
		pos = buf.readBlockPos();
	}
	
	@Override
	public void serverExecute(PacketContext ctx)
	{
		var s = ctx.getSender();
		if(s == null) return;
		
		var be = s.level().getBlockEntity(pos);
		if(be == null) return;
		
		var pkt = be.getUpdatePacket();
		if(pkt == null) return;
		
		s.connection.send(pkt);
	}
}