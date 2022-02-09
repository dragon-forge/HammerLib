package org.zeith.hammerlib.api.io.serializers;

import com.mojang.math.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Vector3d.class)
public class Vector3dSerializer
		implements INBTSerializer<Vector3d>
{
	@Override
	public void serialize(CompoundTag nbt, String key, Vector3d value)
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
	public Vector3d deserialize(CompoundTag nbt, String key)
	{
		if(nbt.contains(key, Tag.TAG_COMPOUND))
		{
			CompoundTag tag = nbt.getCompound(key);
			return new Vector3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return null;
	}
}