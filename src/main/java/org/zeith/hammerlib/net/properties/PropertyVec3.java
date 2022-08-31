package org.zeith.hammerlib.net.properties;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.util.java.DirectStorage;

public class PropertyVec3
		extends PropertyBase<Vec3>
{
	public PropertyVec3(DirectStorage<Vec3> value)
	{
		super(Vec3.class, value);
	}
	
	public PropertyVec3()
	{
		super(Vec3.class);
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
}