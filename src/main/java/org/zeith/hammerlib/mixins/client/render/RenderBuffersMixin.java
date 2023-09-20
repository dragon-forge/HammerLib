package org.zeith.hammerlib.mixins.client.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.*;
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
			method = "lambda$new$1",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;waterMask()Lnet/minecraft/client/renderer/RenderType;")
	)
	private void addGlint(Object2ObjectLinkedOpenHashMap map, CallbackInfo ci)
	{
		put(map, RenderCustomGlint.glint());
		put(map, RenderCustomGlint.glintDirect());
		put(map, RenderCustomGlint.glintTranslucent());
		put(map, RenderCustomGlint.entityGlint());
		put(map, RenderCustomGlint.entityGlintDirect());
	}
}