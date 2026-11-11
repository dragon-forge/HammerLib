package org.zeith.hammerlib.mixins.bs.kubejs;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.core.RecipeManagerKJS;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RecipesKubeEvent.class, remap = false)
public class RecipeEventJSMixin
{
	@Inject(
			method = "post",
			at = @At("TAIL")
	)
	public void modify_HammerLib(RecipeManagerKJS recipeManager, Map<ResourceLocation, JsonElement> datapackRecipeMap, CallbackInfo ci)
	{
//		RecipeHelper.injectRecipes(mgr, ((ContextAwareReloadListenerAccessor) mgr).getConditionContext());
//		HammerLib.LOG.info("Performed HammerLib recipe injection into KubeJS recipe system.");
	}
}