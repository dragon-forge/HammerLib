package com.zeitheron.hammercore.api.events;

import com.zeitheron.hammercore.utils.color.ColorHelper;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WorldTintEvent
		extends Event
{
	private int baseColor = 0xFFFFFF;
	private float intensity = 0F;
	private IntList modifiers;

	private float saturation;

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

	public void setSaturation(float saturation)
	{
		this.saturation = saturation;
	}

	public void multSaturation(float mult)
	{
		saturation *= mult;
	}

	public float getFinalSaturation()
	{
		return saturation;
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