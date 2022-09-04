package org.zeith.hammerlib.mixins;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.core.RecipeHelper;
import org.zeith.hammerlib.util.java.Cast;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Shadow @Final private ICondition.IContext context;
	
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
}