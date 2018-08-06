package com.zeitheron.hammercore.proxy;

import com.zeitheron.hammercore.bookAPI.fancy.GuiHammerManual;

import net.minecraft.client.Minecraft;

public class BookProxy_Client extends BookProxy_Common
{
	@Override
	public void openNewBook()
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiHammerManual());
	}
	
	@Override
	public void init()
	{
	}
}