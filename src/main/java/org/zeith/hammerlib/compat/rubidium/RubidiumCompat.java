package org.zeith.hammerlib.compat.rubidium;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.client.CustomFoilConfigs;
import org.zeith.hammerlib.client.adapter.ChatMessageAdapter;
import org.zeith.hammerlib.client.render.TintingVertexConsumer;
import org.zeith.hammerlib.compat.base.BaseCompat;
import org.zeith.hammerlib.compat.base._hl.BaseHLCompat;
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
		if(ModHelper.isModLoaded("embeddium"))
		{
			HammerLib.LOG.info("Detected Rubidium from Embeddium. Disabling safeguard.");
			return; // Embeddium does a compat wrapper for any other vertex consumer.
		}
		
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			CustomFoilConfigs.rubidiumInstaller = this::reload;
			reload();
		});
		
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () ->
		{
			HammerLib.LOG.fatal("You tried to start a dedicated server with Rubidium installed. This is probably not a good idea.");
		});
		
		var url = "https://www.curseforge.com/minecraft/mc-mods/embeddium";
		
		var curseforgeUri = Component.literal("Embeddium")
				.withStyle(s -> s.withColor(ChatFormatting.BLUE)
						.withUnderlined(true)
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open webpage."))));
		
		ChatMessageAdapter.sendOnFirstWorldLoad(Component.literal("WARNING: HammerLib has limited some of it's functions due to Rubidium. We recommend using ")
				.append(curseforgeUri)
				.append(" instead, as it does causes less issues.")
		);
	}
	
	public void reload()
	{
		if(doesRubidiumSupportHL() && !CustomFoilConfigs.disable_with_rubidium)
			enableRubidiumTint();
		else
			useFallbackRubidiumTint();
	}
	
	public void enableRubidiumTint()
	{
		TintingVertexConsumer.TINT4f = TintingVertexConsumerRB::new;
		TintingVertexConsumer.TINT1i = TintingVertexConsumerRB::new;
		TintingVertexConsumer.tintingEnabled = true;
		HammerLib.LOG.info("Using Rubidium-adapted vertex consumers.");
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