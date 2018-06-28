package com.zeitheron.hammercore.world;

import java.util.HashSet;
import java.util.Set;

import com.zeitheron.hammercore.world.gen.IWorldGenFeature;

public class WorldGenRegistry
{
	private static final Set<IWorldGenFeature> features = new HashSet<>();
	
	public static void registerFeature(IWorldGenFeature feature)
	{
		if(feature != null)
			features.add(feature);
	}
	
	public static Set<IWorldGenFeature> listFeatures()
	{
		return features;
	}
}