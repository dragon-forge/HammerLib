package org.zeith.hammerlib.mixins;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
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
	public ICondition.IContext context;
	
	@Shadow
	public Map<ResourceLocation, Recipe<?>> byName;
	
	@Unique
	private final Map<ResourceLocation, List<ResourceLocation>> hammerLib$SpoofByName = SpoofRecipesEvent.gather();
	
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
	
	public Map<ResourceLocation, List<ResourceLocation>> isrm$getSpoofedRecipesHL()
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