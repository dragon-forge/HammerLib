package org.zeith.hammerlib.compat.rubidium;

import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.client.CustomFoilConfigs;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.util.colors.ColorHelper;

@BaseCompat.LoadCompat(
		modid = "rubidium",
		compatType = BaseHLCompat.class
)
public class RubidiumCompat
		extends BaseHLCompat
{
	public RubidiumCompat()
	{
		CustomFoilConfigs.rubidiumInstaller = this::reload;
		reload();
	}
	
	public void reload()
	{
		useFallbackRubidiumTint();
	}
	
	public void useFallbackRubidiumTint()
	{
		// Pass-through the tinting part.
		TintingVertexConsumer.TINT4f = (vc, r, g, b, a) -> vc;
		TintingVertexConsumer.TINT1i = (vc, rgba) -> vc;
		TintingVertexConsumer.tintingEnabled = false;
		HammerLib.LOG.info("Using fallback vertex consumers since Rubidium is detected.");
	}
	
	// Mixin target for Rubidium (or any patcher in the future to enable tinting)
	public static boolean doesRubidiumSupportHL()
	{
		// If we have the class, try enabling support.
		try
		{
			Class.forName("net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter");
			return true;
		} catch(Throwable e)
		{
		}
		
		return false;
	}
}