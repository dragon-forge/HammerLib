package com.zeitheron.hammercore.net;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks an {@link IPacket} or @{@link com.zeitheron.hammercore.net.transport.ITransportAcceptor} class to run on main thread
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface MainThreaded
{
	/**
	 * @return Should the packet execute from main thread?
	 */
	boolean value() default true;
}