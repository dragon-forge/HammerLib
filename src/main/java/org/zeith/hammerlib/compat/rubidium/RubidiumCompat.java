package org.zeith.hammerlib.compat.rubidium;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.client.CustomFoilConfigs;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
import org.zeith.hammerlib.util.colors.ColorHelper;
import org.zeith.hammerlib.util.mcf.ModHelper;

@BaseCompat.LoadCompat(
		modid = "rubidium",
		compatType = BaseHLCompat.class,
		shouldLoad = @OnlyIf(owner = ModHelper.class, member = "isClient")
)
public class RubidiumCompat
		extends BaseHLCompat
{
	public RubidiumCompat()
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			CustomFoilConfigs.rubidiumInstaller = this::reload;
			reload();
		});
		
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () ->
		{
			HammerLib.LOG.fatal("You tried to start a dedicated server with Rubidium installed. This is probably not a good idea.");
		});
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