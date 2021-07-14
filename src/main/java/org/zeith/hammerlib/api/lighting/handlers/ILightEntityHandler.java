package org.zeith.hammerlib.api.lighting.handlers;

import net.minecraft.entity.Entity;
import org.zeith.hammerlib.api.lighting.ColoredLight;

public interface ILightEntityHandler
{
	ColoredLight createLight(Entity entity);

	default void remove(int persistent)
	{
	}

	default void update(Entity entity)
	{
	}
}