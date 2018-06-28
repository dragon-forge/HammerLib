package com.zeitheron.hammercore.utils;

import com.zeitheron.hammercore.net.HCNet;
import com.zeitheron.hammercore.net.internal.PacketAdvancementToast;

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
			if(!done)
				for(String criteria : mp.getAdvancements().getProgress(adv).getRemaningCriteria())
					mp.getAdvancements().getProgress(adv).grantCriterion(criteria);
			/** Possible fix to infinite advancement toasts? */
			if(!done && mp.getAdvancements().getProgress(adv).isDone())
				HCNet.INSTANCE.sendTo(new PacketAdvancementToast(advancement), mp);
		}
	}
}