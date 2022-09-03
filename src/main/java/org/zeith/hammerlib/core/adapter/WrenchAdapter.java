package org.zeith.hammerlib.core.adapter;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.api.wrench.IWrenchItem;

@Mod.EventBusSubscriber
public class WrenchAdapter
{
	@SubscribeEvent
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock evt)
	{
		if(IWrenchItem.performWrenchAction(new UseOnContext(evt.getLevel(), evt.getEntity(), evt.getHand(), evt.getItemStack(), evt.getHitVec())))
		{
			evt.setCancellationResult(InteractionResult.SUCCESS);
			evt.setCanceled(true);
		}
	}
}