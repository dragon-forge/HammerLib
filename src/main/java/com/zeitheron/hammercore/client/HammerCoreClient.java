package com.zeitheron.hammercore.client;

import com.zeitheron.hammercore.event.client.ClientLoadedInEvent;
import com.zeitheron.hammercore.netv2.HCV2Net;
import com.zeitheron.hammercore.netv2.internal.PacketPing;
import com.zeitheron.hammercore.netv2.internal.PacketPlayerReady;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class HammerCoreClient
{
	public static long ping;
	
	public static void runPingTest()
	{
		HCV2Net.INSTANCE.sendToServer(new PacketPing(System.currentTimeMillis()));
	}
	
	int pingTimer = 40;
	
	boolean renderedWorld = false;
	
	@SubscribeEvent
	public void clientTick(RenderWorldLastEvent e)
	{
		if(!renderedWorld)
		{
			HCV2Net.INSTANCE.sendToServer(new PacketPlayerReady());
			MinecraftForge.EVENT_BUS.post(new ClientLoadedInEvent());
			renderedWorld = true;
		}
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent cte)
	{
		if(cte.phase != Phase.START)
			return;
		
		boolean inWorld = Minecraft.getMinecraft().world != null;
		if(renderedWorld && !inWorld)
			renderedWorld = inWorld;
		
		if(renderedWorld && pingTimer-- <= 0)
		{
			pingTimer = 40;
			runPingTest();
		}
	}
}