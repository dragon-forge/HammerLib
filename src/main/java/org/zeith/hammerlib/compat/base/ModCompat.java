package org.zeith.hammerlib.compat.base;

import org.zeith.hammerlib.annotations.Ref;

import java.lang.annotation.*;

/**
 * An annotation to specify that a class should be loaded only when a certain mod is present.
 *
 * <p>This can be useful for creating compatibility between mods, where certain features or behaviors
 * should only be enabled if a specific mod is present in the game.
 *
 * <p>To use this annotation, simply annotate a class with it and specify the mod ID that the class
 * should be loaded for using the {@link #modid()} attribute with {@link #type()} as it's super type.
 *
 * <pre>
 * {@code
 *
 * // Specify that this class provides block compatibility for the "examplemod" mod.
 * {@literal @}ModCompat(modid = "examplemod", type = BaseHLCompat.class)
 * public class ExampleHLCompatClass extends BaseHLCompat {
 *     // class contents here
 * }
 * }
 * </pre>
 *
 * <p>You can also use the {@link #shouldLoad()} attribute to specify conditions under which the annotated
 * class should be loaded. This attribute takes an {@link Ref} annotation as its value, which allows you
 * to specify the owner class and member name of a boolean value that should be checked before loading the
 * class. If the boolean value is `true`, the class will be loaded; if it is `false`, the class will not
 * be loaded.
 *
 * <pre>
 * {@code
 *
 * // Only load this class if the "COMPAT_ENABLED" field in the ExampleModConfig class is set to true.
 * {@literal @}ModCompat(modid = "examplemod", type = BaseHLCompat.class, shouldLoad = @Ref(value = ExampleModConfig.class, field = "COMPAT_ENABLED"))
 * public class ExampleCompatClass extends BaseHLCompat {
 *     // class contents here
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModCompat
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
	Class<? extends BaseCompat<?>> type();
	
	/**
	 * Specifies the conditions under which the annotated class should be loaded.
	 *
	 * @return A {@link Ref} annotation specifying the owner class and field name of a boolean value that should be checked before loading the class
	 */
	Ref shouldLoad() default @Ref(value = Boolean.class, field = "TRUE");
}