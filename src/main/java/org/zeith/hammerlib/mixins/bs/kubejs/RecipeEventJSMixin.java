package org.zeith.hammerlib.mixins.bs.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeEventJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;

import java.util.HashMap;

@Mixin(value = RecipeEventJS.class, remap = false)
public class RecipeEventJSMixin
{
	@ModifyVariable(
			method = "post",
			at = @At(value = "INVOKE",
					target = "Ljava/util/HashMap;<init>()V",
					shift = At.Shift.BY,
					by = 2),
			slice = @Slice(
					from = @At(value = "INVOKE",
							target = "Ljava/util/concurrent/atomic/AtomicInteger;<init>(I)V"),
					to = @At(value = "INVOKE",
							target = "Lcom/google/gson/JsonObject;addProperty(Ljava/lang/String;Ljava/lang/Boolean;)V")
			),
			index = 9
	)
	public HashMap modify_HammerLib(HashMap recipes)
	{
		int ps = recipes.size();
		RecipeHelper.injectRecipesCustom(recipes);
		HammerLib.LOG.info("Hello KubeJS, this is your 'friend', HammerLib! I injected "
				+ (recipes.size() - ps)
				+ " recipes in your flawed recipe registration system! :)");
		return recipes;
	}
}