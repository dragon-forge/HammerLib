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
public class RecipeManagerMixin
{
	@Shadow(remap = false)
	@Final
	private ICondition.IContext context;
	
	@Shadow
	public Map<ResourceLocation, Recipe<?>> byName;
	
	private Map<ResourceLocation, List<ResourceLocation>> hammerLibSpoofByName = SpoofRecipesEvent.gather();
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void replaceRecipeId(ResourceLocation id, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir)
	{
		if(hammerLibSpoofByName.containsKey(id))
		{
			var recipe = findFirstRecipe_HL(hammerLibSpoofByName.getOrDefault(id, List.of(id)));
			if(recipe.isPresent()) cir.setReturnValue(recipe);
			else HammerLib.LOG.error("Failed to locate recipe with mapping " + id + "=" + hammerLibSpoofByName.get(id));
		}
	}
	
	private Optional<? extends Recipe<?>> findFirstRecipe_HL(Collection<ResourceLocation> rl)
	{
		return rl.stream()
				.map(byName::get)
				.filter(Objects::nonNull)
				.findFirst();
	}
	
	@Inject(
			method = "apply*",
			at = @At("TAIL")
	)
	public void reloadRecipes_HammerLib(Map<ResourceLocation, JsonElement> recipes,
										ResourceManager manager,
										ProfilerFiller profiler,
										CallbackInfo ci)
	{
		RecipeManager mgr = Cast.cast(this);
		RecipeHelper.injectRecipes(mgr, context);
	}
	
	public Map<ResourceLocation, List<ResourceLocation>> isrm$getSpoofedRecipes()
	{
		return hammerLibSpoofByName;
	}
}