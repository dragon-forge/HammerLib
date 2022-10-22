package org.zeith.hammerlib.util.mcf;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.net.packets.PacketAdvancementToast;

public class AdvancementUtils
{
	public static boolean isAdvancementCompleted(ResourceLocation advancement, Player player)
	{
		if(player instanceof ServerPlayer mp)
		{
			var adv = mp.server.getAdvancements().getAdvancement(advancement);
			if(adv == null) return false;
			return mp.getAdvancements().getOrStartProgress(adv).isDone();
		}
		return false;
	}
	
	public static void completeAdvancement(ResourceLocation advancement, Player player)
	{
		if(player instanceof ServerPlayer mp)
		{
			Advancement adv = mp.server.getAdvancements().getAdvancement(advancement);
			if(adv == null) return;
			
			var prog = mp.getAdvancements().getOrStartProgress(adv);
			
			boolean done = prog.isDone();
			if(!done)
				for(String criteria : prog.getRemainingCriteria())
					prog.grantProgress(criteria);
			
			/* Possible fix to infinite advancement toasts? */
			if(!done && prog.isDone()) Network.sendTo(new PacketAdvancementToast(advancement), mp);
		}
	}
}