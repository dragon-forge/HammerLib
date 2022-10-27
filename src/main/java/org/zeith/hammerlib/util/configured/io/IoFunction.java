package org.zeith.hammerlib.util.configured.io;

import java.io.IOException;

@FunctionalInterface
public interface IoFunction<K, R>
{
	R apply(K key) throws IOException;
}