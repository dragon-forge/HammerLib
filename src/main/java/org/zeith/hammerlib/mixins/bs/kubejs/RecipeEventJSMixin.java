package org.zeith.hammerlib.mixins.bs.kubejs;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.core.RecipeManagerKJS;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;

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
		var mgr = ((RecipeManager) recipeManager);
		mgr.recipes = RecipeHelper.performInjectionWizardry(mgr, mgr.recipes);
		HammerLib.LOG.info("Performed HammerLib recipe injection into KubeJS recipe system.");
	}
}