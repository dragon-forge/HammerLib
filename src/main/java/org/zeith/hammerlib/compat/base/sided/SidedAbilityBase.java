package org.zeith.hammerlib.compat.base.sided;

import com.google.common.base.Suppliers;
import net.minecraftforge.fml.DistExecutor;
import org.zeith.hammerlib.util.java.Cast;

import java.util.function.Supplier;

/**
 * A base class for abilities that have different implementations for the client and server sides of a
 * game.
 *
 * <p>This class provides a framework for creating abilities that have different implementations for the
 * client and server sides of a game. It allows you to create a single ability class that can be used
 * on either side of the game, while still being able to specify separate implementations for each side.
 *
 * <p>To use this class, simply extend it and override the {@link #forClient()} method to return a
 * supplier of the client-side implementation of the ability. Then, you can use the {@link #client()}
 * method to get a supplier of the appropriate implementation of the ability for the current side of the
 * game.
 *
 * <pre>
 * {@code
 *
 * public class ExampleSidedAbility extends SidedAbilityBase<ExampleSidedAbility> {
 *
 *     // Override the forClient() method to return a supplier of the client-side implementation of the ability.
 *     protected Supplier<Supplier<ExampleSidedAbility> forClient() {
 *         return () -> () -> new ExampleSidedAbility() {
 *             // Client-side implementation of the ability goes here.
 *         };
 *     }
 *
 *     // Use the client() method to get a supplier of the appropriate implementation of the ability for the current side.
 *     public void doSomething() {
 *         ExampleSidedAbility ability = Cast.get2(client());
 *         // Use the ability instance to perform the desired behavior.
 *     }
 * }
 * }
 * </pre>
 *
 * <p>Note: The {@link #client()} method returns a supplier of a supplier of the ability. This is to
 * ensure that the correct implementation is returned even if the {@link #client()} method is called
 * before the ability has been fully initialized. To get the actual ability instance, you should use
 * the {@code Cast.get2()} method to unwrap the supplier.
 *
 * <pre>
 * {@code
 *
 * ExampleSidedAbility ability = Cast.get2(exampleSidedAbility.client());
 * }
 * </pre>
 *
 * @param <CLIENT>
 * 		the type of the ability
 */
public abstract class SidedAbilityBase<CLIENT extends SidedAbilityBase<? extends CLIENT>>
{
	/**
	 * A supplier of the appropriate implementation of this ability for the client side of the game.
	 *
	 * <p>This field is initialized using the {@link DistExecutor#unsafeRunForDist(Supplier, Supplier)}
	 * method, which ensures that the correct implementation is returned based on the current side of the
	 * game. On the client side, it returns a supplier of the client-side implementation of the ability.
	 * On the server side, it returns a supplier that always returns `null`.
	 *
	 * <p>To get the actual ability instance, you should use the {@code Cast.get2()} method to unwrap the
	 * supplier.
	 *
	 * <pre>
	 * {@code
	 *
	 * ExampleSidedAbility ability = Cast.get2(exampleSidedAbility.client);
	 * }
	 * </pre>
	 */
	public final Supplier<Supplier<CLIENT>> client = DistExecutor.unsafeRunForDist(() -> () -> Suppliers.memoize(() -> forClient().get()), () -> () -> () -> () -> null);
	
	/**
	 * Constructs a new instance of this ability.
	 *
	 * <p>This constructor resolves the {@link #client} field immediately. On the server side, it is
	 * always `null`, while on the client side, it should not be `null`, unless this ability doesn't
	 * have client-specific needs. However, in this case, there is no real need to use `SidedAbilityBase`
	 * for non-sided abilities.
	 */
	public SidedAbilityBase()
	{
		// Resolve the client field immediately. On server, it is always NULL,
		// for client, it should not be null, unless this ability doesn't have client-specific needs.
		// But then there is no real need to use SidedAbility for non-sided abilities.
		Cast.get2(client());
	}
	
	/**
	 * Returns a supplier of the client-side implementation of this ability.
	 *
	 * <p>This method should be overridden to return a supplier of the client-side implementation of this
	 * ability. It will be called when the {@link #client} field is initialized on the client side of the
	 * game.
	 *
	 * <pre>
	 * {@code
	 *
	 * protected Supplier<Supplier<ExampleSidedAbility> forClient() {
	 *     return () -> () -> new ExampleSidedAbility() {
	 *         // Client-side implementation of the ability goes here.
	 *     };
	 * }
	 * }
	 * </pre>
	 *
	 * @return a supplier of the client-side implementation of this ability
	 */
	protected abstract Supplier<Supplier<CLIENT>> forClient();
	
	/**
	 * Returns a supplier of the appropriate implementation of this ability for the current side of the game.
	 *
	 * <p>This method returns a supplier of a supplier of the ability. This is to ensure that the correct
	 * implementation is returned even if this method is called before the ability has been fully initialized.
	 * To get the actual ability instance, you should use the {@code Cast.get2()} method to unwrap the
	 * supplier.
	 *
	 * <pre>
	 * {@code
	 *
	 * ExampleSidedAbility ability = Cast.get2(exampleSidedAbility.client());
	 * }
	 * </pre>
	 *
	 * @return a supplier of the appropriate implementation of this ability for the current side of the game
	 *
	 * @see org.zeith.hammerlib.util.java.Cast#get2(Supplier)
	 */
	public Supplier<Supplier<CLIENT>> client()
	{
		return client;
	}
}