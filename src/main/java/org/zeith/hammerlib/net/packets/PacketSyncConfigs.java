package org.zeith.hammerlib.net.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.zeith.hammerlib.api.config.IConfigRoot;
import org.zeith.hammerlib.core.adapter.ConfigAdapter;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.hammerlib.util.java.ReflectionUtil;

import java.util.Map;

@MainThreaded
public class PacketSyncConfigs
		implements IPacket
{
	private Map<Class<? extends IConfigRoot>, IConfigRoot> toSync;
	private Map<Class<? extends IConfigRoot>, CompoundTag> fromSync;

	public PacketSyncConfigs(Map<Class<? extends IConfigRoot>, IConfigRoot> sync)
	{
		this.toSync = sync;
	}

	public PacketSyncConfigs()
	{
	}

	@Override
	public void write(FriendlyByteBuf buf)
	{
		buf.writeMap(toSync, (b, t) -> b.writeUtf(t.getCanonicalName()), (b, v) ->
		{
			CompoundTag nbt = new CompoundTag();
			v.toNetwork(nbt);
			b.writeNbt(nbt);
		});
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		fromSync = buf.readMap(b -> ReflectionUtil.fetchClass(b.readUtf()), FriendlyByteBuf::readNbt);
	}

	public Map<Class<? extends IConfigRoot>, CompoundTag> data()
	{
		return fromSync;
	}

	@Override
	public void clientExecute(PacketContext ctx)
	{
		ConfigAdapter.handleClientsideSync(this);
	}
}