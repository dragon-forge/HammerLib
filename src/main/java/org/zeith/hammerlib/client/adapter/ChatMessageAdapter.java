package org.zeith.hammerlib.client.adapter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.zeith.hammerlib.api.proxy.IProxy;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter
{
	private static final List<Component> messages = new ArrayList<>();
	
	public static void sendOnFirstWorldLoad(Component message)
	{
		synchronized(messages)
		{
			messages.add(message);
		}
	}
	
	static
	{
		IProxy.runOn(Dist.CLIENT, () -> () ->
				MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent evt) ->
				{
					var mc = Minecraft.getInstance();
					if(mc.level != null)
					{
						while(!messages.isEmpty())
						{
							mc.chatListener.handleSystemMessage(messages.remove(0), false);
						}
					}
				})
		);
	}
}