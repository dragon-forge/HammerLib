package org.zeith.hammerlib.util.mcf.itf;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkable<T>
{
	void toNetwork(FriendlyByteBuf buf, T obj);
	
	T fromNetwork(FriendlyByteBuf buf);
}