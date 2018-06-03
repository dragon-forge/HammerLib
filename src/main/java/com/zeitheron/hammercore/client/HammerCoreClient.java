package com.zeitheron.hammercore.client;

import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.internal.V2PacketPing;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class HammerCoreClient
{
	public static long ping;
	
	public static void runPingTest()
	{
		HCV2Net.INSTANCE.sendToServer(new V2PacketPing(System.currentTimeMillis()));
	}
	
	int pingTimer = 40;
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent e)
	{
		boolean inWorld = Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().isGamePaused();
		
		if(e.phase == Phase.START)
		{
			if(inWorld)
			{
				if(pingTimer-- <= 0)
				{
					pingTimer = 40;
					runPingTest();
				}
			} else
				pingTimer = 40;
		}
	}
}