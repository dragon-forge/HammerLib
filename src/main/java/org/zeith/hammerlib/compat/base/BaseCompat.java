package org.zeith.hammerlib.compat.base;

import org.zeith.hammerlib.annotations.OnlyIf;

import java.lang.annotation.*;
import java.util.Optional;

/**
 * A base class for creating compatibility classes that can provide special features or behaviors to
 * other mods or parts of the game.
 *
 * <p>Compatibility classes are used to enable interoperability between mods or to provide additional
 * functionality to the game. They can provide special features or behaviors through a system of
 * "abilities", which are special interfaces or classes that represent a specific capability or feature.
 *
 * <p>To create a compatibility class, simply extend this class and override the {@link #getAbility(Ability)}
 * method to return an {@link Optional} containing the desired ability. Then, use the {@link LoadCompat}
 * annotation to specify that the class should be loaded when a certain mod is present in the game.
 *
 * <pre>
 * {@code
 *
 * // Specify that this class provides block compatibility for the "examplemod" mod.
 * @LoadCompat(modid = "examplemod", compatType = BaseHLCompat.class)
 * public class ExampleHLCompatClass extends BaseHLCompat {
 * 	   public final ExampleAbility example = new ExampleAbilityImpl();
 *
 *     // Override the getAbility() method to return the desired ability.
 *     @Override
 *     public <R> Optional<R> getAbility(Ability<R> ability) {
 *         return ability.findIn(example);
 *     }
 *
 *     // Ability implementation goes here.
 *     public static class ExampleAbilityImpl implements ExampleAbility {
 *         // Ability implementation goes here.
 *     }
 * }
 * }
 * </pre>
 */
public abstract class BaseCompat<T extends BaseCompat<T>>
{
	protected final CompatContext context;
	
	public BaseCompat(CompatContext ctx)
	{
		this.context = ctx;
	}
	
	/**
	 * Returns an {@link Optional} containing the specified ability, if present.
	 *
	 * <p>Abilities are special interfaces or classes that represent a specific capability or feature that
	 * a compatibility class can provide. This method allows you to retrieve a specific ability from a
	 * compatibility class.
	 *
	 * <p>To use this method, pass it the desired ability type as a parameter. If the compatibility class
	 * provides the specified ability, the method should return an {@link Optional} containing the ability
	 * instance. If the compatibility class does not provide the specified ability, the method should return
	 * an empty {@link Optional}.
	 *
	 * <pre>
	 * {@code
	 *
	 * // Retrieve the ExampleAbility ability from an ExampleHLCompatClass instance.
	 * Optional<BloomAbilityBase> ability = exampleHLCompatClass.getAbility(HLAbilities.BLOOM);
	 *
	 * // Use the ability instance, if present.
	 * ability.ifPresent(a -> a.doSomething());
	 * }
	 * </pre>
	 *
	 * @param ability
	 * 		the type of ability to retrieve
	 * @param <R>
	 * 		the type of the ability
	 *
	 * @return an {@link Optional} containing the specified ability, if present; otherwise, an empty
	 * {@link Optional}
	 */
	public <R> Optional<R> getAbility(Ability<R> ability)
	{
		return Optional.empty();
	}
	
	/**
	 * An annotation to specify that a class should be loaded only when a certain mod is present.
	 *
	 * <p>This can be useful for creating compatibility between mods, where certain features or behaviors
	 * should only be enabled if a specific mod is present in the game.
	 *
	 * <p>To use this annotation, simply annotate a class with it and specify the mod ID that the class
	 * should be loaded for using the {@link #modid()} attribute with {@link #compatType()} as it's super type.
	 *
	 * <pre>
	 * {@code
	 *
	 * // Specify that this class provides block compatibility for the "examplemod" mod.
	 * {@literal @}LoadCompat(modid = "examplemod", compatType = BaseHLCompat.class)
	 * public class ExampleHLCompatClass extends BaseHLCompat {
	 *     // class contents here
	 * }
	 * }
	 * </pre>
	 *
	 * <p>You can also use the {@link #shouldLoad()} attribute to specify conditions under which the annotated
	 * class should be loaded. This attribute takes an {@link OnlyIf} annotation as its value, which allows you
	 * to specify the owner class and member name of a boolean value that should be checked before loading the
	 * class. If the boolean value is `true`, the class will be loaded; if it is `false`, the class will not
	 * be loaded.
	 *
	 * <pre>
	 * {@code
	 *
	 * // Only load this class if the "enableCompat" field in the ExampleModConfig class is set to true.
	 * {@literal @}LoadCompat(modid = "examplemod", compatType = BaseHLCompat.class, shouldLoad = @OnlyIf(owner = ExampleModConfig.class, member = "enableCompat"))
	 * public class ExampleCompatClass extends BaseHLCompat {
	 *     // class contents here
	 * }
	 * }
	 * </pre>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface LoadCompat
	{
		/**
		 * The required mod ID for the compatibility to be loaded in.
		 *
		 * @return the mod ID that the annotated class should be loaded for
		 */
		String modid();
		
		/**
		 * The type of compatibility class that the annotated class represents.
		 *
		 * @return the type of compatibility that the annotated class provides
		 */
		Class<? extends BaseCompat<?>> compatType();
		
		/**
		 * Specifies the conditions under which the annotated class should be loaded.
		 *
		 * @return an {@link OnlyIf} annotation specifying the owner class and member name of a boolean value
		 * that should be checked before loading the class
		 */
		OnlyIf shouldLoad() default @OnlyIf(owner = Boolean.class, member = "TRUE");
	}
}