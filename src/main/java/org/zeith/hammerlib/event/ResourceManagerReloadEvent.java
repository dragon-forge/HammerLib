package org.zeith.hammerlib.event;

import net.minecraft.resources.IResourceManager;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.resource.IResourceType;

import java.util.function.Predicate;

public class ResourceManagerReloadEvent
		extends Event
{
	public final Predicate<IResourceType> type;
	public final IResourceManager manager;

	public ResourceManagerReloadEvent(Predicate<IResourceType> type, IResourceManager manager)
	{
		this.type = type;
		this.manager = manager;
	}

	public boolean isType(IResourceType type)
	{
		return this.type.test(type);
	}
}