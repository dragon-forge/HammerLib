package org.zeith.hammerlib.mixins;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
public abstract class RecipeManagerMixin
		implements ISpoofedRecipeManager
{
	@Shadow
	public RecipeMap recipes;
	
	@Unique
	private final Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> hammerLib$SpoofByName = SpoofRecipesEvent.gather();
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_replaceRecipeId(ResourceKey<Recipe<?>> id, CallbackInfoReturnable<Optional<? extends RecipeHolder<?>>> cir)
	{
		if(id == null || !hammerLib$SpoofByName.containsKey(id)) return;
		var recipe = findFirstRecipeHL(hammerLib$SpoofByName.getOrDefault(id, List.of(id)));
		if(recipe.isPresent()) cir.setReturnValue(recipe);
		else HammerLib.LOG.error("Failed to locate recipe with mapping " + id + "=" + hammerLib$SpoofByName.get(id));
	}
	
	@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
			at = @At(value = "RETURN"),
			cancellable = true
	)
	public void HammerLib_reloadRecipes(ResourceManager resources, ProfilerFiller profiler, CallbackInfoReturnable<RecipeMap> cir)
	{
		cir.setReturnValue(RecipeHelper.performInjectionWizardry(Cast.cast(this), cir.getReturnValue()));
	}
	
	public Map<ResourceKey<Recipe<?>>, List<ResourceKey<Recipe<?>>>> isrm$getSpoofedRecipesHL()
	{
		return hammerLib$SpoofByName;
	}
	
	public Optional<? extends RecipeHolder<?>> isrm$findFirstRecipeHL(Collection<ResourceKey<Recipe<?>>> rl)
	{
		return rl.stream()
				.map(recipes::byKey)
				.filter(Objects::nonNull)
				.findFirst();
	}
}