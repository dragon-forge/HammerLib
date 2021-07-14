package org.zeith.hammerlib.annotations;

import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotating a class that implements {@link IRecipeProvider} will enable Hammer
 * Lib to create a new instance and invoke the
 * {@link IRecipeProvider#provideRecipes(RegisterRecipesEvent)}
 * when needed.
 */
@Retention(RUNTIME)
@Target({
		TYPE,
		METHOD
})
public @interface ProvideRecipes
{
	String modid() default "";
}