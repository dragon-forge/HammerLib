package org.zeith.hammerlib.mixins.client;

import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.client.model.ExcludeBlockStateModelEvent;
import org.zeith.hammerlib.util.java.Cast;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ModelBakery.class)
public class ModelBakeryMixin
{
	private final List<ModelResourceLocation> blockedHLModels = new ArrayList<>();

	@Inject(
			method = "uploadTextures",
			at = @At("HEAD")
	)
	public void injectProcessLoading(TextureManager p_119299_, ProfilerFiller p_119300_, CallbackInfoReturnable<AtlasSet> cir)
	{
		blockedHLModels.clear();
		HammerLib.postEvent(new ExcludeBlockStateModelEvent(Cast.cast(this), blockedHLModels::add));
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