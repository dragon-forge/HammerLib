package org.zeith.hammerlib.mixins.bs.kubejs;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.mixins.RecipeManagerAccessor;
import org.zeith.hammerlib.util.java.Cast;

import java.util.*;

@Mixin(value = RecipesEventJS.class, remap = false)
public class RecipeEventJSMixin
{
	private RecipeManager recipeManager_HL;
	private final Set<ResourceLocation> removedRecipes_HL = new HashSet<>();
	
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
		
		removedRecipes_HL.clear();
		RecipeHelper.injectRecipesCustom(recipes,
				removedRecipes_HL,
				Cast.optionally(
								Optional.ofNullable(recipeManager_HL)
										.or(() -> Optional.ofNullable(ServerLifecycleHooks.getCurrentServer()).map(MinecraftServer::getRecipeManager)),
								RecipeManagerAccessor.class)
						.map(RecipeManagerAccessor::getContext)
						.orElse(ICondition.IContext.EMPTY)
		);
		
		recipes.keySet().removeAll(removedRecipes_HL);
		HammerLib.LOG.info("Hello KubeJS, this is your 'friend', HammerLib! I injected "
				+ (recipes.size() - ps)
				+ " recipes in your flawed recipe registration system! :)");
		return recipes;
	}
	
	@Inject(
			method = "lambda$post$4",
			at = @At("HEAD"),
			cancellable = true
	)
	private void removeCustomRecipes_HL(MutableInt removed, RecipeJS recipe, CallbackInfoReturnable<Boolean> cir)
	{
		if(removedRecipes_HL.contains(recipe.id))
		{
			removed.increment();
			cir.setReturnValue(false);
		}
	}
}