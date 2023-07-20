package org.zeith.hammerlib.mixins.bs.kubejs;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.mixins.RecipeManagerAccessor;

import java.util.Map;

@Mixin(value = RecipesEventJS.class, remap = false)
public class RecipeEventJSMixin
{
	@Inject(
			method = "post",
			at = @At("TAIL")
	)
	public void modify_HammerLib(RecipeManager mgr, Map<ResourceLocation, JsonElement> datapackRecipeMap, CallbackInfo ci)
	{
		RecipeHelper.injectRecipes(mgr, ((RecipeManagerAccessor) mgr).getContext());
		HammerLib.LOG.info("Performed HammerLib recipe injection into KubeJS recipe system.");
	}
}