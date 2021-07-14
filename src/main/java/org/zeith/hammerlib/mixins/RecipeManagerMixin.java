package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.zeith.hammerlib.core.RecipeHelper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin
{
	@Redirect(
			method = "apply",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;stream()Ljava/util/stream/Stream;"
			)
	)
	public Stream<Map.Entry<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>>> applyStream(Set<Map.Entry<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>>> set)
	{
		Map<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>> map = Maps.newHashMap();
		set.forEach(e -> map.put(e.getKey(), e.getValue()));
		RecipeHelper.hookRecipesASM(map);
		return map.entrySet().stream();
	}
}