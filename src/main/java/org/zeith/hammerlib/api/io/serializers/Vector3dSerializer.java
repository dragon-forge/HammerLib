package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(Vector3d.class)
public class Vector3dSerializer
		implements INBTSerializer<Vector3d>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, Vector3d value)
	{
		if(value != null)
		{
			CompoundNBT tag = new CompoundNBT();
			tag.putDouble("x", value.x);
			tag.putDouble("y", value.y);
			tag.putDouble("z", value.z);
			nbt.put(key, tag);
		}
	}

	@Override
	public Vector3d deserialize(CompoundNBT nbt, String key)
	{
		if(nbt.contains(key, Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT tag = nbt.getCompound(key);
			return new Vector3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
		}
		return null;
	}
}