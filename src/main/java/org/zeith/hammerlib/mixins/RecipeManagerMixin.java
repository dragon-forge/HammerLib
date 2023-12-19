package org.zeith.hammerlib.mixins;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
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
		extends SimpleJsonResourceReloadListener
{
	@Shadow
	public Map<ResourceLocation, Recipe<?>> byName;
	
	@Unique
	private final Map<ResourceLocation, List<ResourceLocation>> hl$SpoofByName = SpoofRecipesEvent.gather();
	
	public RecipeManagerMixin(Gson p_10768_, String p_10769_)
	{
		super(p_10768_, p_10769_);
	}
	
	@Inject(
			method = "byKey",
			at = @At("HEAD"),
			cancellable = true
	)
	private void HammerLib_replaceRecipeId(ResourceLocation id, CallbackInfoReturnable<Optional<? extends Recipe<?>>> cir)
	{
		if(hl$SpoofByName.containsKey(id))
		{
			var recipe = HammerLib_findFirstRecipe(hl$SpoofByName.getOrDefault(id, List.of(id)));
			if(recipe.isPresent()) cir.setReturnValue(recipe);
			else HammerLib.LOG.error("Failed to locate recipe with mapping " + id + "=" + hl$SpoofByName.get(id));
		}
	}
	
	private Optional<? extends Recipe<?>> HammerLib_findFirstRecipe(Collection<ResourceLocation> rl)
	{
		return rl.stream()
				.map(byName::get)
				.filter(Objects::nonNull)
				.findFirst();
	}
	
	@Inject(
			method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At("TAIL")
	)
	public void HammerLib_reloadRecipes(CallbackInfo ci)
	{
		RecipeManager mgr = Cast.cast(this);
		RecipeHelper.injectRecipes(mgr, conditionContext);
	}
	
	public Map<ResourceLocation, List<ResourceLocation>> isrm$getSpoofedRecipes()
	{
		return hl$SpoofByName;
	}
}