package org.zeith.hammerlib.api.io.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.api.io.NBTSerializer;
import org.zeith.hammerlib.util.mcf.Resources;

@NBTSerializer(ResourceLocation.class)
public class ResourceLocationSerializer
		implements INBTSerializer<ResourceLocation>
{
	@Override
	public void serialize(HolderLookup.Provider provider, String key, @NotNull ResourceLocation value, CompoundTag nbt)
	{
		if(value != null)
			nbt.putString(key, value.toString());
	}

	@Override
	public ResourceLocation deserialize(HolderLookup.Provider provider, String key, CompoundTag nbt)
	{
		return nbt.contains(key, Tag.TAG_STRING) ? Resources.locationOrNull(nbt.getString(key)) : null;
	}
}