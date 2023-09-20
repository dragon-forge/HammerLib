package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.*;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.client.render.RenderCustomGlint;

@Mixin(RenderBuffers.class)
public abstract class RenderBuffersMixin
{
	@Shadow
	private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> pMapBuilders, RenderType pRenderType)
	{
	}
	
	@Inject(
			method = "put",
			at = @At("HEAD")
	)
	private static void addGlint_1_19_2(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, RenderType type, CallbackInfo ci)
	{
		// Adds before the glint (so custom glint goes first, then all other glint buffers)
		if(type == RenderType.glint())
		{
			put(map, RenderCustomGlint.armorGlint());
			put(map, RenderCustomGlint.armorEntityGlint());
			put(map, RenderCustomGlint.glint());
			put(map, RenderCustomGlint.glintDirect());
			put(map, RenderCustomGlint.glintTranslucent());
			put(map, RenderCustomGlint.entityGlint());
			put(map, RenderCustomGlint.entityGlintDirect());
			
			LogManager.getLogger("HammerLib").info("Registered custom glint render buffers.");
		}
	}
}