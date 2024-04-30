package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableMultimap;
import com.google.gson.Gson;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.api.level.ISpoofedRecipeManager;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.event.recipe.SpoofRecipesEvent;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;

@Mixin(RecipeManager.class)
@Implements({
		@Interface(iface = ISpoofedRecipeManager.class, prefix = "isrm$")
})
@Debug(export = true)
public abstract class RecipeManagerMixin
		extends SimpleJsonResourceReloadListener
		implements ISpoofedRecipeManager
{
	@Shadow
	public Map<ResourceLocation, Recipe<?>> byName;
	
	@Unique
	private final Map<ResourceLocation, List<ResourceLocation>> hammerLib$SpoofByName = SpoofRecipesEvent.gather();
	
	public RecipeManagerMixin(Gson gson, String dir)
	{
		super(gson, dir);
	}
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_replaceRecipeId(ResourceLocation id, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir)
	{
		if(id == null || !hammerLib$SpoofByName.containsKey(id)) return;
		var recipe = findFirstRecipeHL(hammerLib$SpoofByName.getOrDefault(id, List.of(id)));
		if(recipe.isPresent()) cir.setReturnValue(recipe);
		else HammerLib.LOG.error("Failed to locate recipe with mapping " + id + "=" + hammerLib$SpoofByName.get(id));
	}
	
	@Inject(
			method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/crafting/RecipeManager;makeConditionalOps()Lnet/neoforged/neoforge/common/conditions/ConditionalOps;"
			)
	)
	public void HammerLib_reloadRecipes(CallbackInfo ci,
										@Local ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder,
										@Local com.google.common.collect.ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder1
	)
	{
		RecipeManager mgr = Cast.cast(this);
		RecipeHelper.injectRecipes(mgr, getContext(), holder ->
		{
		
		}, id ->
		{
		
		});
	}
	
	public Map<ResourceLocation, List<ResourceLocation>> isrm$getSpoofedRecipesHL()
	{
		return hammerLib$SpoofByName;
	}
	
	private Optional<? extends Recipe<?>> isrm$findFirstRecipeHL(Collection<ResourceLocation> rl)
	{
		return rl.stream()
				.map(byName::get)
				.filter(Objects::nonNull)
				.findFirst();
	}
}