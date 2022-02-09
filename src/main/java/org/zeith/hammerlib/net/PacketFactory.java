package org.zeith.hammerlib.net;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.unsafe.UnsafeHacks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketFactory
{
	private static final BiMap<String, Class<IPacket>> PACKET_TYPES = HashBiMap.create();
	private static final BiMap<Class<IPacket>, String> PACKET_TYPES_I = PACKET_TYPES.inverse();
	private static final Map<Class<IPacket>, Supplier<? extends IPacket>> FACTORIES = new HashMap<>();

	public static byte[] readArray(FriendlyByteBuf buf)
	{
		short size = buf.readShort();
		return buf.readByteArray(size);
	}

	public static void writeArray(FriendlyByteBuf buf, byte[] data)
	{
		buf.writeShort(data.length).writeBytes(data);
	}

	public static String getPacketId(IPacket packet)
	{
		if(packet == null)
			return "";
		Class<? extends IPacket> type = packet.getClass();
		return PACKET_TYPES_I.getOrDefault(type, type.getName());
	}

	public static Class<? extends IPacket> getPacketById(String id)
	{
		if(id == null || id.isEmpty())
			return null;
		if(PACKET_TYPES.containsKey(id))
			return PACKET_TYPES.get(id);
		try
		{
			return Class.forName(id).asSubclass(IPacket.class);
		} catch(Throwable e)
		{
			return null;
		}
	}

	public static IPacket createEmpty(String id)
	{
		Class<? extends IPacket> pkt = getPacketById(id);
		if(pkt == null)
			return null;
		if(FACTORIES.containsKey(pkt))
			return FACTORIES.get(pkt).get();
		try
		{
			return IPacket.class.cast(UnsafeHacks.newInstance(pkt));
		} catch(Throwable e)
		{
			return null;
		}
	}
}