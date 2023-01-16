package org.zeith.hammerlib.api.recipes;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * RecipeBuilderExtension is an abstract class that serves as a base for creating and registering recipe builder extensions.
 * It contains a static registry for storing the factory function for creating instances of the extension.
 * The {@link #attach(RegisterRecipesEvent)} method is used to attach the registered extensions to a RegisterRecipesEvent.
 *
 * @author Zeith
 */
public abstract class RecipeBuilderExtension
{
	private static final Supplier<Map<Class<?>, Function<RegisterRecipesEvent, ? extends RecipeBuilderExtension>>> REGISTRY = Suppliers.memoize(() -> Util.make(new ConcurrentHashMap<>(), map ->
	{
		for(Class<? extends RecipeBuilderExtension> type : ScanDataHelper.lookupAnnotatedTypes(RegisterExt.class, RecipeBuilderExtension.class))
		{
			try
			{
				var ctor = type.getDeclaredConstructor(RegisterRecipesEvent.class);
				ctor.setAccessible(true);
				
				Function<RegisterRecipesEvent, ? extends RecipeBuilderExtension> generator = evt ->
				{
					try
					{
						return ctor.newInstance(evt);
					} catch(InstantiationException | IllegalAccessException | InvocationTargetException e)
					{
						HammerLib.LOG.error("Failed to attach " + type.getName() + " to recipe registration.");
					}
					return null;
				};
				
				map.put(type, generator);
			} catch(ReflectiveOperationException roe)
			{
				HammerLib.LOG.error("Failed to register " + type.getName() + " constructor.");
			}
		}
	}));
	
	/**
	 * Event object used for registering recipes.
	 */
	protected final RegisterRecipesEvent event;
	
	/**
	 * Constructor for creating a new instance of a recipe builder extension.
	 *
	 * @param event
	 * 		The event object used for registering recipes.
	 */
	public RecipeBuilderExtension(RegisterRecipesEvent event)
	{
		this.event = event;
	}
	
	/**
	 * Registers a new recipe builder extension to the registry.
	 *
	 * @param type
	 * 		The class of the recipe builder extension to be registered.
	 * @param factory
	 * 		The factory function used to create instances of the recipe builder extension.
	 */
	public static <T extends RecipeBuilderExtension> void register(Class<T> type, Function<RegisterRecipesEvent, T> factory)
	{
		// Never allow base type, or any other classes, that are not of RecipeBuilderExtension!
		if(RecipeBuilderExtension.class.equals(type) || !RecipeBuilderExtension.class.isAssignableFrom(type)) return;
		REGISTRY.get().put(type, factory);
	}
	
	/**
	 * Attaches all registered recipe builder extensions to the given event object.
	 *
	 * @param evt
	 * 		The event object to attach the recipe builder extensions to.
	 *
	 * @return A map containing all attached recipe builder extensions, with their corresponding class as the key.
	 */
	public static Map<Class<?>, RecipeBuilderExtension> attach(RegisterRecipesEvent evt)
	{
		return REGISTRY.get().entrySet()
				.stream()
				.map(e ->
				{
					try
					{
						return Tuples.mutable(e.getKey(), e.getValue().apply(evt));
					} catch(Exception err)
					{
						HammerLib.LOG.error("Failed to decode recipe builder extension of type " + e.getKey());
						return null;
					}
				})
				.filter(e -> e != null && e.a().isInstance(e.b())) // value instanceof class
				.collect(Collectors.toMap(Tuple2::a, Tuple2::b));
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface RegisterExt
	{
	}
}