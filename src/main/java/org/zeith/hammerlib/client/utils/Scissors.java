package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

/**
 * Adapted GL scissor for minecraft pixel resolution and adjusts (0;0) to
 * left-top corner.
 */
public class Scissors
{
	public static void begin(int x, int y, int width, int height)
	{
		Window wnd = Minecraft.getInstance().getWindow();
		
		int sw = wnd.getWidth();
		int sh = wnd.getHeight();
		
		float dw = wnd.getGuiScaledWidth();
		float dh = wnd.getGuiScaledHeight();
		
		x = Math.round(sw * (x / dw));
		y = Math.round(sh * (y / dh));
		
		width = Math.round(sw * (width / dw));
		height = Math.round(sh * (height / dh));
		
		RenderSystem.enableScissor(x, sh - height - y, width, height);
	}
	
	public static void end()
	{
		RenderSystem.disableScissor();
	}
}