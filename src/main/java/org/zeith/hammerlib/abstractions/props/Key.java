package org.zeith.hammerlib.abstractions.props;

import net.minecraft.resources.ResourceLocation;

public record Key<T>(ResourceLocation name, Class<T> type)
{
	public static <T> Key<T> of(ResourceLocation id, Class<T> type)
	{
		return new Key<>(id, type);
	}
}