package com.zeitheron.hammercore.netv2.transport;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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
}