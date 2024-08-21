package org.zeith.hammerlib.net.properties;

import com.zeitheron.hammercore.utils.java.DirectStorage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class PropertyVec3
		extends PropertyBase<Vec3d>
{
	public PropertyVec3(DirectStorage<Vec3d> value)
	{
		super(Vec3d.class, value);
	}
	
	public PropertyVec3()
	{
		super(Vec3d.class);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		Vec3d value = this.value.get();
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
			value.set(new Vec3d(x, y, z));
		} else value.set(null);
	}
}