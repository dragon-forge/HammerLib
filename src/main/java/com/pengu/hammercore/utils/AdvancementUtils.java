package com.pengu.hammercore.utils;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketAdvancementToast;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class AdvancementUtils
{
	public static boolean isAdvancementCompleted(ResourceLocation advancement, EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP && !player.world.isRemote && player.getServer() != null)
		{
			EntityPlayerMP mp = (EntityPlayerMP) player;
			MinecraftServer mc = player.getServer();
			Advancement adv = mc.getAdvancementManager().getAdvancement(advancement);
			if(adv == null)
				return false;
			return mp.getAdvancements().getProgress(adv).isDone();
		}
		return false;
	}
	
	public static void completeAdvancement(ResourceLocation advancement, EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP && !player.world.isRemote && player.getServer() != null)
		{
			EntityPlayerMP mp = (EntityPlayerMP) player;
			MinecraftServer mc = player.getServer();
			Advancement adv = mc.getAdvancementManager().getAdvancement(advancement);
			if(adv == null)
				return;
			boolean done = mp.getAdvancements().getProgress(adv).isDone();
			for(String criteria : mp.getAdvancements().getProgress(adv).getRemaningCriteria())
				mp.getAdvancements().getProgress(adv).grantCriterion(criteria);
			if(!done)
				HCNetwork.manager.sendTo(new PacketAdvancementToast(advancement), mp);
		}
	}
}