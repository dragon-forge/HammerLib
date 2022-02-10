package org.zeith.hammerlib.mixins;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.java.Cast;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Inject(
			method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At("TAIL")
	)
	public void reloadRecipes_HammerLib(Map<ResourceLocation, JsonElement> jsonparseexception, ResourceManager resourcelocation, ProfilerFiller entry, CallbackInfo ci)
	{
		RecipeManager mgr = Cast.cast(this);
		RecipeHelper.injectRecipes(mgr);
	}
}
