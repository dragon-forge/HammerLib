package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyVec3
		implements IProperty<Vec3>
{
	final DirectStorage<Vec3> value;

	public PropertyVec3(DirectStorage<Vec3> value)
	{
		this.value = value;
	}

	public PropertyVec3()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<Vec3> getType()
	{
		return Vec3.class;
	}

	@Override
	public Vec3 set(Vec3 value)
	{
		Vec3 pv = this.value.get();
		if(!Objects.equals(pv, value))
		{
			this.value.set(value);
			markChanged(true);
		}
		return pv;
	}

	boolean changed;

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
	public void write(FriendlyByteBuf buf)
	{
		Vec3 value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null)
		{
			buf.writeDouble(value.x);
			buf.writeDouble(value.y);
			buf.writeDouble(value.z);
		}
	}

	@Override
	public void read(FriendlyByteBuf buf)
	{
		if(buf.readBoolean())
		{
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			value.set(new Vec3(x, y, z));
		} else value.set(null);
	}

	@Override
	public Vec3 get()
	{
		return value.get();
	}

	PropertyDispatcher dispatcher;

	@Override
	public PropertyDispatcher getDispatcher()
	{
		return dispatcher;
	}

	@Override
	public void setDispatcher(PropertyDispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}
}