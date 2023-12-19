package org.zeith.hammerlib.client.adapter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

import java.util.*;
import java.util.function.Consumer;

public class ChatMessageAdapter
{
	private static final List<Component> messages = new ArrayList<>();
	
	public static void sendOnFirstWorldLoad(Component message)
	{
		messages.add(message);
	}
	
	static
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			Consumer<TickEvent.ClientTickEvent> listener = evt ->
			{
				var mc = Minecraft.getInstance();
				if(mc.level != null)
				{
					while(!messages.isEmpty())
					{
						mc.chatListener.handleSystemMessage(messages.remove(0), false);
					}
				}
			};
			NeoForge.EVENT_BUS.addListener(listener);
		});
	}
}