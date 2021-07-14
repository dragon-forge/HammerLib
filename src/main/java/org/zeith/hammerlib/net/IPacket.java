package org.zeith.hammerlib.net;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.util.java.Threading;

public interface IPacket
{
	default void write(PacketBuffer buf)
	{
	}

	default void read(PacketBuffer buf)
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