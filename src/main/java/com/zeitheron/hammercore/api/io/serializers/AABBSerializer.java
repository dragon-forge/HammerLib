package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(AxisAlignedBB.class)
public class AABBSerializer
		implements INBTSerializer<AxisAlignedBB>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull AxisAlignedBB value)
	{
		if(value != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setDouble("mx", value.minX);
			tag.setDouble("my", value.minY);
			tag.setDouble("mz", value.minZ);
			tag.setDouble("xx", value.maxX);
			tag.setDouble("xy", value.maxY);
			tag.setDouble("xz", value.maxZ);
			nbt.setTag(key, tag);
		}
	}
	
	@Override
	public AxisAlignedBB deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, Constants.NBT.TAG_COMPOUND))
		{
			NBTTagCompound tag = nbt.getCompoundTag(key);
			return new AxisAlignedBB(
					tag.getDouble("mx"),
					tag.getDouble("my"),
					tag.getDouble("mz"),
					tag.getDouble("xx"),
					tag.getDouble("xy"),
					tag.getDouble("xz")
			);
		}
		return null;
	}
}