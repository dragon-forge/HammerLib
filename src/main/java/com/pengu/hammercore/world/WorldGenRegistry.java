package com.pengu.hammercore.world;

import java.util.HashSet;
import java.util.Set;

import com.pengu.hammercore.world.gen.iWorldGenFeature;

public class WorldGenRegistry
{
	private static final Set<iWorldGenFeature> features = new HashSet<>();
	
	public static void registerFeature(iWorldGenFeature feature)
	{
		if(feature != null)
			features.add(feature);
	}
	
	public static Set<iWorldGenFeature> listFeatures()
	{
		return features;
	}
}