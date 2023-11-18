package com.zeitheron.hammercore.api.io.serializers;

import com.zeitheron.hammercore.api.io.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

@NBTSerializer(ResourceLocation.class)
public class ResourceLocationSerializer
		implements INBTSerializer<ResourceLocation>
{
	@Override
	public void serialize(NBTTagCompound nbt, String key, @Nonnull ResourceLocation value)
	{
		if(value != null)
			nbt.setString(key, value.toString());
	}
	
	@Override
	public ResourceLocation deserialize(NBTTagCompound nbt, String key)
	{
		return nbt.hasKey(key, Constants.NBT.TAG_STRING) ? new ResourceLocation(nbt.getString(key)) : null;
	}
}