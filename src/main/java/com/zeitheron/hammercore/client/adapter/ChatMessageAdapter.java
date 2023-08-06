package com.zeitheron.hammercore.client.adapter;

import net.minecraft.util.text.ITextComponent;

import java.util.*;

public class ChatMessageAdapter
{
	public static final List<ITextComponent> messages = new ArrayList<>();
	
	public static void sendOnFirstWorldLoad(ITextComponent message)
	{
		messages.add(message);
	}
}