package com.zeitheron.hammercore.api.events;

import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.function.Predicate;

public class ResourceManagerReloadEvent
		extends Event
{
	final IResourceManager manager;
	final Predicate<IResourceType> resourceType;

	public ResourceManagerReloadEvent(IResourceManager manager, Predicate<IResourceType> resourceType)
	{
		this.manager = manager;
		this.resourceType = resourceType;
	}

	public IResourceManager getManager()
	{
		return manager;
	}

	public Predicate<IResourceType> getResourceType()
	{
		return resourceType;
	}

	public boolean isType(IResourceType type)
	{
		return resourceType.test(type);
	}
}