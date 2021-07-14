package org.zeith.hammerlib.api.lighting;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import org.zeith.hammerlib.util.colors.ColorHelper;

@OnlyIn(Dist.CLIENT)
public class WorldTintEvent
		extends Event
{
	private int baseColor = 0xFFFFFF;
	private float intensity = 0F;
	private IntList modifiers;

	public WorldTintEvent(IntList modifiers)
	{
		this.modifiers = modifiers;
	}

	public void addModifierColor(int rgb)
	{
		this.modifiers.add(rgb);
	}

	public int getFinalColor()
	{
		if(!modifiers.isEmpty())
		{
			float rt = 0, gt = 0, bt = 0;

			for(int i : modifiers)
			{
				rt += ColorHelper.getRed(i);
				gt += ColorHelper.getGreen(i);
				bt += ColorHelper.getBlue(i);
			}

			return ColorHelper.packRGB(rt / modifiers.size(), gt / modifiers.size(), bt / modifiers.size());
		}
		return baseColor;
	}

	/**
	 * Please use maxIntensity whenever possible instead.
	 */
	public void setIntensity(float intensity)
	{
		this.intensity = intensity;
	}

	public void maxIntensity(float newIntensity)
	{
		this.intensity = Math.max(this.intensity, newIntensity);
	}

	public float getIntensity()
	{
		return intensity;
	}
}