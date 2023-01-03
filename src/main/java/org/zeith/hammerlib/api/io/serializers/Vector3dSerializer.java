package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Vec3.class)
public class Vector3dSerializer
		implements INBTSerializer<Vec3>
{
	@Override
	public void serialize(CompoundTag nbt, String key, Vec3 value)
	{
		if(value != null)
		{
			CompoundTag tag = new CompoundTag();
			tag.putDouble("x", value.x);
			tag.putDouble("y", value.y);
			tag.putDouble("z", value.z);
			nbt.put(key, tag);
		}
	}

	@Override
	public Vec3 deserialize(CompoundTag nbt, String key)
	{
		if(nbt.contains(key, Tag.TAG_COMPOUND))
		{
			CompoundTag tag = nbt.getCompound(key);
			return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return null;
	}
}