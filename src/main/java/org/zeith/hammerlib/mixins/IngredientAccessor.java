package org.zeith.hammerlib.mixins;

import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public interface IngredientAccessor
{
	@Invoker("<init>")
	static Ingredient createIngredient(Stream<? extends Ingredient.Value> p_43907_) {throw new UnsupportedOperationException();}
	
	@Invoker("<init>")
	static Ingredient createIngredient(Ingredient.Value[] p_301044_) {throw new UnsupportedOperationException();}
}
