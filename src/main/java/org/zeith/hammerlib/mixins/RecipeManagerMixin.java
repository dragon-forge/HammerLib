package org.zeith.hammerlib.mixins;

import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.crafting.conditions.ICondition;
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
public abstract class RecipeManagerMixin
		implements ISpoofedRecipeManager
{
	@Shadow(remap = false)
	@Final
	private ICondition.IContext context;
	
	@Shadow
	public Map<ResourceLocation, Recipe<?>> byName;
	
	@Shadow
	public Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;
	@Unique
	private final Multimap<ResourceLocation, ResourceLocation> hammerLib$SpoofByName = SpoofRecipesEvent.gather();
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_replaceRecipeId(ResourceLocation id, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir)
	{
		if(id == null || !hammerLib$SpoofByName.containsKey(id)) return;
		
		var spofed = hammerLib$SpoofByName.get(id);
		if(spofed.isEmpty()) return;
		
		var recipe = findFirstRecipeHL(spofed);
		if(recipe.isPresent()) cir.setReturnValue(recipe);
		else HammerLib.LOG.error("Failed to locate recipe with mapping {}={}", id, hammerLib$SpoofByName.get(id));
	}
	
	@Inject(
			method = "apply*",
			at = @At("TAIL")
	)
	public void HammerLib_reloadRecipes(Map<ResourceLocation, JsonElement> recipes,
										ResourceManager manager,
										ProfilerFiller profiler,
										CallbackInfo ci)
	{
		RecipeManager mgr = Cast.cast(this);
		RecipeHelper.injectRecipes(mgr, context);
	}
	
	public Multimap<ResourceLocation, ResourceLocation> isrm$getSpoofedRecipesHL()
	{
		return hammerLib$SpoofByName;
	}
	
	public Optional<? extends Recipe<?>> isrm$findFirstRecipeHL(Collection<ResourceLocation> ids)
	{
		return ids.stream()
				.map(byName::get)
				.filter(Objects::nonNull)
				.findFirst();
	}
}