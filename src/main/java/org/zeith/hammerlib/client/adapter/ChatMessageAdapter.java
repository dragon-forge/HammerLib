package org.zeith.hammerlib.client.adapter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;
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
			MinecraftForge.EVENT_BUS.addListener(listener);
		});
	}
}