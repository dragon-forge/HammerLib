package org.zeith.hammerlib.net.properties;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import org.zeith.hammerlib.util.java.DirectStorage;

import java.util.Objects;

public class PropertyVector3d
		implements IProperty<Vector3d>
{
	final DirectStorage<Vector3d> value;

	public PropertyVector3d(DirectStorage<Vector3d> value)
	{
		this.value = value;
	}

	public PropertyVector3d()
	{
		this(DirectStorage.allocate());
	}

	@Override
	public Class<Vector3d> getType()
	{
		return Vector3d.class;
	}

	@Override
	public Vector3d set(Vector3d value)
	{
		Vector3d pv = this.value.get();
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
	public void write(PacketBuffer buf)
	{
		Vector3d value = this.value.get();
		buf.writeBoolean(value != null);
		if(value != null)
		{
			buf.writeDouble(value.x);
			buf.writeDouble(value.y);
			buf.writeDouble(value.z);
		}
	}

	@Override
	public void read(PacketBuffer buf)
	{
		if(buf.readBoolean())
		{
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			value.set(new Vector3d(x, y, z));
		} else value.set(null);
	}

	@Override
	public Vector3d get()
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