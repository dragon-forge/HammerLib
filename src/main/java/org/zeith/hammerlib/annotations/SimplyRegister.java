package org.zeith.hammerlib.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
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
}