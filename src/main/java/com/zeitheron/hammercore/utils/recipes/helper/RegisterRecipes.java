package com.zeitheron.hammercore.utils.recipes.helper;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Targets {@link RecipeRegistry} class to register recipes via code.
 **/
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterRecipes
{
	String modid();
}