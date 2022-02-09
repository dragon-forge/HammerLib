package org.zeith.hammerlib.util.colors;

import net.minecraft.util.Mth;

public class Rainbow
{
	public static int doIt(long offset, long fullSycleInMilis)
	{
		float r = 1;
		float g = 1;
		float b = 1;

		long time = Math.abs(System.currentTimeMillis() + offset) % fullSycleInMilis;
		long msPerSector = fullSycleInMilis / 3L;
		int currentSector = Mth.floor((double) time / (double) msPerSector) + 1;
		float sectorProgression = (time % msPerSector) / (float) msPerSector;
		float sectorDepth = sectorProgression * 2F;
		if(sectorDepth > 1)
			sectorDepth = 2 - sectorDepth;

		if(currentSector == 1)
		{
			b = 1 - sectorProgression;
			r = 1;
			g = sectorProgression;
		} else if(currentSector == 2)
		{
			r = 1 - sectorProgression;
			g = 1;
			b = sectorProgression;
		} else if(currentSector == 3)
		{
			g = 1 - sectorProgression;
			b = 1;
			r = sectorProgression;
		}

		return ColorHelper.packRGB(r, g, b);
	}
}