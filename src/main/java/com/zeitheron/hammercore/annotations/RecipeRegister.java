package com.zeitheron.hammercore.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;

/**
 * Marks a method to receive a registration call when
 * {@link com.zeitheron.hammercore.internal.SimpleRegistration#registerConstantRecipes(Class)} is invoked. The
 * method must have only one parameter - {@link List}<{@link IRecipe}><br>
 * The method should not return anything (void)
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface RecipeRegister
{
}