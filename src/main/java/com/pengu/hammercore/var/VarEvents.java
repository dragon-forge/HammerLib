package com.pengu.hammercore.var;

import com.pengu.hammercore.annotations.MCFBus;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@MCFBus
public class VarEvents
{
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		VariableManager.updateManager();
	}
	
	@SubscribeEvent
	public void playerJoin(PlayerLoggedInEvent evt)
	{
		if(evt.player instanceof EntityPlayerMP)
			VariableManager.sendVarsTo((EntityPlayerMP) evt.player);
	}
}