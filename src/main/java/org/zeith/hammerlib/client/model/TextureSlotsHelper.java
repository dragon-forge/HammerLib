package org.zeith.hammerlib.client.model;

import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.Material;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class TextureSlotsHelper
{
	public static TextureSlots replacing(TextureSlots origin, BiFunction<String, Material, Material> replacer)
	{
		return new TextureSlots(origin.resolvedValues)
		{
			@Override
			public @Nullable Material getMaterial(String name)
			{
				return replacer.apply(name, origin.getMaterial(name));
			}
		};
	}
}