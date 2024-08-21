package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public interface IProperty<T>
{
	Class<T> getType();

	T set(T value);

	void markChanged(boolean changed);

	boolean hasChanged();

	void write(PacketBuffer buf);

	void read(PacketBuffer buf)
			throws IOException;

	T get();

	PropertyDispatcher getDispatcher();

	default void notifyDispatcherOfChange()
	{
		PropertyDispatcher dispatcher = getDispatcher();
		if(dispatcher != null) dispatcher.notifyOfChange(this);
	}

	void setDispatcher(PropertyDispatcher dispatcher);
}