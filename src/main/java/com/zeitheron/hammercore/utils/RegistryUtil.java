package com.zeitheron.hammercore.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class RegistryUtil
{
	/**
	 * Peeks into any registry and returns last id for a registered object.
	 * 
	 * @param <K>
	 *            The key, often as {@link ResourceLocation}
	 * @param <V>
	 *            The value
	 * @param reg
	 *            The registry to peek into
	 * @return the last used ID
	 */
	public static <K, V> int getMostRegistryId(RegistryNamespaced<K, V> reg)
	{
		int most = 0;
		for(V v : reg)
			most = Math.max(reg.getIDForObject(v), most);
		return most;
	}
	
	/**
	 * Peeks into any registry and returns next id for a registered object.
	 * 
	 * @param <K>
	 *            The key, often as {@link ResourceLocation}
	 * @param <V>
	 *            The value
	 * @param reg
	 *            The registry to peek into
	 * @return the next ID
	 */
	public static <K, V> int getNextRegistryId(RegistryNamespaced<K, V> reg)
	{
		return getMostRegistryId(reg) + 1;
	}
}