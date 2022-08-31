package org.zeith.hammerlib.net.properties;

import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public abstract class PropertyBase<T>
		implements IProperty<T>
{
	protected PropertyDispatcher dispatcher;
	protected boolean changed;
	protected final DirectStorage<T> value;
	protected final Class<T> type;
	
	public PropertyBase(Class<T> type, DirectStorage<T> value)
	{
		this.type = type;
		this.value = value;
	}
	
	public PropertyBase(Class<T> type)
	{
		this(type, DirectStorage.allocate());
	}
	
	protected boolean differ(T a, T b)
	{
		return !Objects.equals(a, b);
	}
	
	public DirectStorage<T> getStorage()
	{
		return value;
	}
	
	@Override
	public Class<T> getType()
	{
		return type;
	}
	
	@Override
	public T set(T value)
	{
		T pv = this.value.get();
		if(differ(pv, value))
		{
			this.value.set(value);
			markChanged(true);
		}
		return pv;
	}
	
	@Override
	public T get()
	{
		return value.get();
	}
	
	@Override
	public PropertyDispatcher getDispatcher()
	{
		return dispatcher;
	}
	
	@Override
	public void setDispatcher(PropertyDispatcher dispatcher)
	{
		if(this.dispatcher == null)
			this.dispatcher = dispatcher;
	}
	
	@Override
	public void markChanged(boolean changed)
	{
		this.changed = changed;
		if(changed) notifyDispatcherOfChange();
	}
	
	@Override
	public boolean hasChanged()
	{
		return changed;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" + get() + "}";
	}
}