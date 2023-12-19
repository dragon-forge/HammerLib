package org.zeith.hammerlib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.*;
import org.zeith.hammerlib.util.java.Threading;

public interface IPacket
{
	default void write(FriendlyByteBuf buf)
	{
	}

	default void read(FriendlyByteBuf buf)
	{
	}

	default void execute(PacketContext ctx)
	{
		switch(ctx.getSide())
		{
			case CLIENT:
				clientExecute(ctx);
				return;
			default:
				serverExecute(ctx);
				return;
		}
	}

	@OnlyIn(Dist.CLIENT)
	default void clientExecute(PacketContext ctx)
	{
	}

	default void serverExecute(PacketContext ctx)
	{
	}

	default boolean executeOnMainThread()
	{
		return Threading.isMainThreaded(getClass());
	}
}