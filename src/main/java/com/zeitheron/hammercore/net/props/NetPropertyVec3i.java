package com.zeitheron.hammercore.net.props;

import com.zeitheron.hammercore.HammerCore;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class NetPropertyVec3i<T extends Vec3i> extends NetPropertyAbstract<T>
{
	public NetPropertyVec3i(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyVec3i(IPropertyChangeHandler handler, T initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
		{
			nbt.setInteger("x", value.getX());
			nbt.setInteger("y", value.getY());
			nbt.setInteger("z", value.getZ());
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("x"))
		{
			try
			{
				value = (T) new Vec3i(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			} catch(Throwable err)
			{
				try
				{
					value = (T) new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
				} catch(Throwable err2)
				{
					HammerCore.LOG.warn("Unable to read Vec3i subclass!");
				}
			}
		}
	}
}