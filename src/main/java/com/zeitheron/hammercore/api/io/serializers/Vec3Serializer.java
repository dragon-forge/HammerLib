package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(Vec3d.class)
public class Vec3Serializer
		implements INBTSerializer<Vec3d>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull Vec3d value)
	{
		if(value != null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setDouble("x", value.x);
			tag.setDouble("y", value.y);
			tag.setDouble("z", value.z);
			nbt.setTag(key, tag);
		}
	}
	
	@Override
	public Vec3d deserialize(NBTTagCompound nbt, String key)
	{
		if(nbt.hasKey(key, Constants.NBT.TAG_COMPOUND))
		{
			NBTTagCompound tag = nbt.getCompoundTag(key);
			return new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return null;
	}
}