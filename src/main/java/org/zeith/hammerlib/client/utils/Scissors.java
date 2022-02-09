package org.zeith.hammerlib.client.utils;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/**
 * Adapted GL scissor for minecraft pixel resolution and adjusts (0;0) to
 * left-top corner.
 */
public class Scissors
{
	public static void begin()
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	public static void scissor(int x, int y, int width, int height)
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

		GL11.glScissor(x, sh - height - y, width, height);
	}

	public static void end()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
}