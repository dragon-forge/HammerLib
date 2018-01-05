package com.pengu.hammercore.color;

import com.pengu.hammercore.utils.ColorHelper;

public class InterpolationUtil
{
	public static int interpolate(float progress, int... rgbas)
	{
		float per = 1F / rgbas.length;
		float p = (progress * rgbas.length) % 1F;
		int sector = ((int) Math.floor(progress / per)) % rgbas.length;
		int sectorNext = ((int) Math.floor(progress / per + per)) % rgbas.length;
		return interpolate(rgbas[sector], rgbas[sectorNext], p);
	}
	
	public static int interpolate(int a, int b, float progress)
	{
		double v2 = 1 - progress;
		
		int r1 = (int) (ColorHelper.getRed(a) * 255F);
		int g1 = (int) (ColorHelper.getGreen(a) * 255F);
		int b1 = (int) (ColorHelper.getBlue(a) * 255F);
		int a1 = (int) (ColorHelper.getAlpha(a) * 255F);
		
		int r2 = (int) (ColorHelper.getRed(b) * 255F);
		int g2 = (int) (ColorHelper.getGreen(b) * 255F);
		int b2 = (int) (ColorHelper.getBlue(b) * 255F);
		int a2 = (int) (ColorHelper.getAlpha(b) * 255F);
		
		int rd = Math.min(255, (int) (r1 * v2 + r2 * progress));
		int gr = Math.min(255, (int) (g1 * v2 + g2 * progress));
		int bl = Math.min(255, (int) (b1 * v2 + b2 * progress));
		int al = Math.min(255, (int) (a1 * v2 + a2 * progress));
		
		return (al << 24) | (rd << 16) | (gr << 8) | bl;
	}
}