package com.zeitheron.hammercore.api.lighting;

import com.zeitheron.hammercore.api.events.WorldTintEvent;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class WorldTintHandler
{
	private static final IntList modifiers = new IntArrayList();

	public static float tintRed, tintGreen, tintBlue, tintIntensity;

	@SubscribeEvent
	public static void renderTick(TickEvent.RenderTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START)
		{
			modifiers.clear();
			WorldTintEvent e2 = new WorldTintEvent(modifiers);
			MinecraftForge.EVENT_BUS.post(e2);
			int tc = e2.getFinalColor();
			tintIntensity = e2.getIntensity();
			tintRed = ColorHelper.getRed(tc);
			tintGreen = ColorHelper.getGreen(tc);
			tintBlue = ColorHelper.getBlue(tc);
		}
	}
}