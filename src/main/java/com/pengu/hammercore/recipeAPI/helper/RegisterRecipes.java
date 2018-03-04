package com.pengu.hammercore.recipeAPI.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Targets {@link RecipeRegistry} class to register recipes via code.
 **/
@Target(ElementType.TYPE)
public @interface RegisterRecipes
{
	String modid();
}