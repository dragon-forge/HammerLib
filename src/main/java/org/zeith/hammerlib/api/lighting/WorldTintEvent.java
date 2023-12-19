package org.zeith.hammerlib.api.lighting;

import it.unimi.dsi.fastutil.ints.IntList;
import lombok.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.Event;
import org.zeith.hammerlib.util.colors.ColorHelper;

@OnlyIn(Dist.CLIENT)
public class WorldTintEvent
		extends Event
{
	private int baseColor = 0xFFFFFF;
	/**
	 * -- SETTER --
	 * Please use maxIntensity whenever possible instead.
	 */
	@Getter
	@Setter
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
	
	public void maxIntensity(float newIntensity)
	{
		this.intensity = Math.max(this.intensity, newIntensity);
	}
}