package com.zeitheron.hammercore.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.relauncher.Side;

public class PipelineProxy_Client extends PipelineProxy_Common
{
	@Override
	public Side getGameSide()
	{
		return Side.CLIENT;
	}
	
	@Override
	public void sendToServer(Packet packet)
	{
		EntityPlayerSP sp = Minecraft.getMinecraft().player;
		if(sp != null)
			sp.connection.sendPacket(packet);
	}
	
	@Override
	public void runFromMainThread(Side side, Runnable task)
	{
		if(side == Side.CLIENT)
			Minecraft.getMinecraft().addScheduledTask(task);
		else
			super.runFromMainThread(side, task);
	}
}