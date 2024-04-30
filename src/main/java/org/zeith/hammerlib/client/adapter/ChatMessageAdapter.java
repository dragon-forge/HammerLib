package org.zeith.hammerlib.client.adapter;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter
{
	private static final List<Component> messages = new ArrayList<>();
	
	public static void sendOnFirstWorldLoad(Component message)
	{
		messages.add(message);
	}
	
	public static Component clientTick()
	{
		if(!ChatMessageAdapter.messages.isEmpty())
			return ChatMessageAdapter.messages.remove(0);
		return null;
	}
}