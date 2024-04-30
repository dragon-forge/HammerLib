package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Vec3.class)
public class Vec3Serializer
		implements INBTSerializer<Vec3>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull Vec3 value, CompoundTag nbt)
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
	public Vec3 deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		if(nbt.contains(key, Tag.TAG_COMPOUND))
		{
			CompoundTag tag = nbt.getCompound(key);
			return new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return null;
	}
}