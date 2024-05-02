package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;

@NBTSerializer(ResourceKey.class)
public class ResourceKeySerializer
		implements INBTSerializer<ResourceKey>
{
	@Override
	public void serialize(CompoundTag nbt, String key, @NotNull ResourceKey value)
	{
		if(value != null)
		{
			CompoundTag tag = new CompoundTag();
			tag.putString("Registry", value.registry().toString());
			tag.putString("Location", value.location().toString());
			nbt.putString(key, value.toString());
		}
	}
	
	@Override
	public ResourceKey deserialize(CompoundTag nbt, String key)
	{
		if(!nbt.contains(key, Tag.TAG_COMPOUND)) return null;
		var tag = nbt.getCompound(key);
		var reg = ResourceLocation.tryParse(tag.getString("Registry"));
		var loc = ResourceLocation.tryParse(tag.getString("Location"));
		return reg != null && loc != null ? ResourceKey.create(
				ResourceKey.createRegistryKey(reg),
				loc
		) : null;
	}
}