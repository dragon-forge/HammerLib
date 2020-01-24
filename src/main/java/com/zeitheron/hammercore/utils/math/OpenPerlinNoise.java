package com.zeitheron.hammercore.utils.math;

import java.util.Random;

public class OpenPerlinNoise
{
	private final OpenSimplexNoise[] noiseLevels;
	private final int levels;

	public OpenPerlinNoise(Random seed, int levelsIn)
	{
		this.levels = levelsIn;
		this.noiseLevels = new OpenSimplexNoise[levelsIn];
		for(int i = 0; i < levelsIn; ++i)
			this.noiseLevels[i] = new OpenSimplexNoise(seed);
	}

	public double getValue(double x, double y)
	{
		double d0 = 0.0D;
		double d1 = 1.0D;
		for(int i = 0; i < this.levels; ++i)
		{
			d0 += this.noiseLevels[i].eval(x * d1, y * d1) / d1;
			d1 /= 2.0D;
		}
		return d0;
	}

	public double getValue(double x, double y, double z)
	{
		double d0 = 0.0D;
		double d1 = 1.0D;
		for(int i = 0; i < this.levels; ++i)
		{
			d0 += this.noiseLevels[i].eval(x * d1, y * d1, z * d1) / d1;
			d1 /= 2.0D;
		}
		return d0;
	}
}