package com.zeitheron.hammercore.net.props;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

public class NetPropertyAABB extends NetPropertyAbstract<AxisAlignedBB>
{
	public NetPropertyAABB(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyAABB(IPropertyChangeHandler handler, AxisAlignedBB initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
		{
			nbt.setDouble("MinX", value.minX);
			nbt.setDouble("MinY", value.minY);
			nbt.setDouble("MinY", value.minZ);
			nbt.setDouble("MaxX", value.maxX);
			nbt.setDouble("MaxY", value.maxY);
			nbt.setDouble("MaxZ", value.maxZ);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = new AxisAlignedBB(nbt.getDouble("MinX"), nbt.getDouble("MinY"), nbt.getDouble("MinZ"), nbt.getDouble("MaxX"), nbt.getDouble("MaxY"), nbt.getDouble("MaxZ"));
	}
}