package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;

public interface IProperty<T>
{
	Class<T> getType();

	T set(T value);

	void markChanged(boolean changed);

	boolean hasChanged();

	void write(FriendlyByteBuf buf);

	void read(FriendlyByteBuf buf);

	T get();

	PropertyDispatcher getDispatcher();

	default void notifyDispatcherOfChange()
	{
		PropertyDispatcher dispatcher = getDispatcher();
		if(dispatcher != null) dispatcher.notifyOfChange(this);
	}

	void setDispatcher(PropertyDispatcher dispatcher);
}