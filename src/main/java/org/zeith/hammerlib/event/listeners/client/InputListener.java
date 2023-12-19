package org.zeith.hammerlib.event.listeners.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import org.zeith.hammerlib.api.blocks.IHitsDifferentTargetBlock;
import org.zeith.hammerlib.mixins.BlockHitResultAccessor;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class InputListener
{
	@SubscribeEvent
	public static void mouseClicked(InputEvent.InteractionKeyMappingTriggered event)
	{
		var mc = Minecraft.getInstance();
		mc.hitResult = alterHitResult(mc.hitResult, mc.level);
	}
	
	public static HitResult alterHitResult(HitResult result, Level level)
	{
		if(result != null && result.getType() == HitResult.Type.BLOCK && result instanceof BlockHitResult res && level != null)
		{
			var state = level.getBlockState(res.getBlockPos());
			if(state.getBlock() instanceof IHitsDifferentTargetBlock ihdtb)
			{
				var np = ihdtb.alterHitPosition(level, res.getBlockPos(), state);
				
				return BlockHitResultAccessor.createBlockHitResult(false,
						res.getLocation()
								.subtract(Vec3.atLowerCornerOf(res.getBlockPos()))
								.add(Vec3.atLowerCornerOf(np)),
						res.getDirection(),
						np,
						res.isInside()
				);
			}
		}
		return result;
	}
}