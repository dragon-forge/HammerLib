package com.zeitheron.hammercore.net.transport;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.IPacket;
import com.zeitheron.hammercore.net.transport.impl.PacketWrapperAcceptor;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class NetTransport
{
	public static final EnumMap<Side, Map<String, TransportSession>> SESSIONS = new EnumMap<>(Side.class);

	public static void indexSession(TransportSession session)
	{
		Map<String, TransportSession> m = SESSIONS.get(session.createSide);
		if(m == null)
			SESSIONS.put(session.createSide, m = new HashMap<>());
		m.put(session.id, session);
	}

	public static TransportSession getSession(Side side, String id)
	{
		Map<String, TransportSession> m = SESSIONS.get(side);
		if(m == null)
			SESSIONS.put(side, m = new HashMap<>());
		return m.get(id);
	}

	public static TransportSessionBuilder builder()
	{
		return new TransportSessionBuilder();
	}

	public static TransportSession wrap(IPacket packet)
	{
		NBTTagCompound nbt = HCNet.writePacket(packet, new NBTTagCompound());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			CompressedStreamTools.write(nbt, new DataOutputStream(baos));
		}
		catch (IOException ioexception)
		{
			throw new EncoderException(ioexception);
		}
		return builder().addData(baos.toByteArray()).setAcceptor(PacketWrapperAcceptor.class).build();
	}
}