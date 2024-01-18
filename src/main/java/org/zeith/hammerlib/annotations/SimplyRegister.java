package org.zeith.hammerlib.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Put this annotation to a static method that has 1 argument:
 * <code>{@link java.util.function.BiConsumer}<{@link net.minecraft.resources.ResourceLocation}, T></code> with the registry entry type want to register.
 * OR assign to the class to let HammerLib register everything inside it.
 */
@Retention(RUNTIME)
@Target({
		TYPE,
		METHOD
})
public @interface SimplyRegister
{
	/**
	 * By adding a prefix to annotation on a class, all of its fields will have its name prefixed by this value
	 * This would also add the
	 */
	String prefix() default "";
	
	/**
	 * By adding this field to annotation, all {@link net.minecraft.world.item.BlockItem}s and {@link net.minecraft.world.item.Items}s will be added to the referenced tab(s).
	 */
	Ref[] creativeTabs() default {};
}