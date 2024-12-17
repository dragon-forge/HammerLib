package org.zeith.hammerlib.compat.base;

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
 * method to return an {@link Optional} containing the desired ability. Then, use the {@link ModCompat}
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
	
}