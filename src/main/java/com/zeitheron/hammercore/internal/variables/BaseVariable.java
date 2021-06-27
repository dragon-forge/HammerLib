package com.zeitheron.hammercore.internal.variables;

import com.zeitheron.hammercore.utils.base.SideLocal;
import net.minecraft.util.ResourceLocation;

/**
 * This class handles most of the underlying code for working with network-based variables.
 * Extend this for adding support to custom variable types.
 */
public abstract class BaseVariable<T>
		implements IVariable<T>
{
	protected final ResourceLocation id;
	protected final SideLocal<T> storage = createStorage();
	protected boolean isDirty;

	public BaseVariable(ResourceLocation id)
	{
		this.id = id;
	}

	protected abstract boolean hasChanged(T old, T updated);

	protected SideLocal<T> createStorage()
	{
		return SideLocal.createEmpty();
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public T get()
	{
		return storage.get();
	}

	@Override
	public void set(T t)
	{
		T value = get();
		if(hasChanged(value, t))
		{
			storage.set(t);
			isDirty = true;
		}
	}

	@Override
	public boolean hasChanged()
	{
		return isDirty;
	}

	@Override
	public void setNotChanged()
	{
		isDirty = false;
	}
}