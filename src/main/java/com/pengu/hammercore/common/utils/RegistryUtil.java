package com.pengu.hammercore.common.utils;

import net.minecraft.util.registry.RegistryNamespaced;

public class RegistryUtil
{
	/**
	 * Peeks into any registry and returns last id for a registered object.
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
	 */
	public static <K, V> int getNextRegistryId(RegistryNamespaced<K, V> reg)
	{
		return getMostRegistryId(reg) + 1;
	}
}