package com.pengu.hammercore.recipeAPI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Targets class to be registered to Hammer Core on PostInit
 **/
@Target(ElementType.TYPE)
public @interface RecipePlugin
{
}