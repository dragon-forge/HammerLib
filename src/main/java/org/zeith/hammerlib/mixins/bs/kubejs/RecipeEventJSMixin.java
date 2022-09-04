package org.zeith.hammerlib.mixins.bs.kubejs;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.mixins.RecipeManagerAccessor;
import org.zeith.hammerlib.util.java.Cast;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = RecipeEventJS.class, remap = false)
public class RecipeEventJSMixin
{
	private RecipeManager recipeManager_HL;
	
	@Inject(
			method = "post",
			at = @At("HEAD")
	)
	private void postPre_HL(RecipeManager recipeManager, Map<ResourceLocation, JsonObject> jsonMap, CallbackInfo ci)
	{
		recipeManager_HL = recipeManager;
	}
	
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
		RecipeHelper.injectRecipesCustom(recipes,
				Cast.optionally(recipeManager_HL, RecipeManagerAccessor.class)
						.map(RecipeManagerAccessor::getContext)
						.orElse(ICondition.IContext.EMPTY)
		);
		HammerLib.LOG.info("Hello KubeJS, this is your 'friend', HammerLib! I injected "
				+ (recipes.size() - ps)
				+ " recipes in your flawed recipe registration system! :)");
		return recipes;
	}
}