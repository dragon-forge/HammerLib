package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.event.client.model.ExcludeBlockStateModelEvent;
import org.zeith.hammerlib.util.java.Cast;

import java.util.ArrayList;
import java.util.List;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin
{
	private final List<ModelResourceLocation> blockedHLModels = new ArrayList<>();

	@Inject(
			method = "processLoading",
			at = @At("HEAD"),
			remap = false
	)
	public void injectProcessLoading(IProfiler profiler, int i, CallbackInfo ci)
	{
		blockedHLModels.clear();
		MinecraftForge.EVENT_BUS.post(new ExcludeBlockStateModelEvent(Cast.cast(this), blockedHLModels::add));
	}

	@Inject(
			method = "loadModel",
			at = @At("HEAD"),
			cancellable = true
	)
	public void injectLoadModel(ResourceLocation rl, CallbackInfo ci)
	{
		if(rl.getClass() == ModelResourceLocation.class && blockedHLModels.contains(rl))
			ci.cancel();
	}
}