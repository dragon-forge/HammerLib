package org.zeith.hammerlib.mixins;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.zeith.hammerlib.event.recipe.RecipeDeserializeEvent;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Inject(
			method = "fromJson",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void fromJsonHL(ResourceLocation id, JsonObject json, CallbackInfoReturnable<IRecipe<?>> cir)
	{
		if(MinecraftForge.EVENT_BUS.post(new RecipeDeserializeEvent(id, json)))
			cir.setReturnValue(null);
	}
}