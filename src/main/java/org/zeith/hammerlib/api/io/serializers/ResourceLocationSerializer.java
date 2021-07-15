package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ResourceLocation.class)
public class ResourceLocationSerializer
		implements INBTSerializer<ResourceLocation>
{
	@Override
	public void serialize(CompoundNBT nbt, String key, ResourceLocation value)
	{
		if(value != null)
			nbt.putString(key, value.toString());
	}

	@Override
	public ResourceLocation deserialize(CompoundNBT nbt, String key)
	{
		return nbt.contains(key, Constants.NBT.TAG_STRING) ? new ResourceLocation(nbt.getString(key)) : null;
	}
}